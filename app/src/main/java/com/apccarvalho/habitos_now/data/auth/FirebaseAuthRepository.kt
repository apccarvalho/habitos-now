package com.apccarvalho.habitos_now.data.auth

import com.apccarvalho.habitos_now.data.model.Usuario
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : AuthRepository {

    override val usuarioAtual: Usuario?
        get() = auth.currentUser?.paraUsuario()

    override suspend fun entrar(email: String, senha: String): Result<Usuario> = executar {
        val resultado = auth.signInWithEmailAndPassword(email, senha).await()
        checkNotNull(resultado.user).paraUsuario()
    }

    override suspend fun criarConta(nome: String, email: String, senha: String): Result<Usuario> =
        executar {
            val user = checkNotNull(auth.createUserWithEmailAndPassword(email, senha).await().user)
            val perfil = UserProfileChangeRequest.Builder().setDisplayName(nome).build()
            user.updateProfile(perfil).await()
            Usuario(uid = user.uid, nome = nome)
        }

    override suspend fun enviarRedefinicaoSenha(email: String): Result<Unit> = executar {
        auth.sendPasswordResetEmail(email).await()
        Unit
    }

    override fun sair() = auth.signOut()

    private fun FirebaseUser.paraUsuario() = Usuario(
        uid = uid,
        nome = displayName?.takeIf { it.isNotBlank() } ?: email.orEmpty().substringBefore("@")
    )

    private suspend fun <T> executar(bloco: suspend () -> T): Result<T> =
        try {
            Result.success(bloco())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(AuthException(traduzir(e)))
        }

    // A senha fraca é uma subclasse de credenciais inválidas, por isso vem antes.
    private fun traduzir(erro: Exception): String = when (erro) {
        is FirebaseAuthWeakPasswordException -> "Senha fraca. Use pelo menos 6 caracteres."
        is FirebaseAuthUserCollisionException -> "Este e-mail já está cadastrado."
        is FirebaseAuthInvalidUserException -> "Não existe conta com este e-mail."
        is FirebaseAuthInvalidCredentialsException -> "E-mail ou senha incorretos."
        is FirebaseNetworkException -> "Sem conexão com a internet. Tente novamente."
        is FirebaseTooManyRequestsException -> "Muitas tentativas. Aguarde um pouco e tente de novo."
        else -> "Não foi possível concluir a operação. Tente novamente."
    }
}
