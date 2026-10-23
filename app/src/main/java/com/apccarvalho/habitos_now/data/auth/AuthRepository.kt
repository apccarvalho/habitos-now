package com.apccarvalho.habitos_now.data.auth

import com.apccarvalho.habitos_now.data.model.Usuario

interface AuthRepository {
    val usuarioAtual: Usuario?
    suspend fun entrar(email: String, senha: String): Result<Usuario>
    suspend fun criarConta(nome: String, email: String, senha: String): Result<Usuario>
    suspend fun enviarRedefinicaoSenha(email: String): Result<Unit>
    fun sair()
}

class AuthException(mensagem: String) : Exception(mensagem)
