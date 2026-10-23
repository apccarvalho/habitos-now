package com.apccarvalho.habitos_now.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.apccarvalho.habitos_now.HabitosNowApp
import com.apccarvalho.habitos_now.data.auth.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun trocarModo(modo: ModoAuth) {
        _uiState.update { it.copy(modo = modo, erros = ErrosAuth(), erroGeral = null) }
    }

    fun onNomeChange(nome: String) {
        _uiState.update { it.copy(nome = nome, erros = it.erros.copy(nome = null), erroGeral = null) }
    }

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, erros = it.erros.copy(email = null), erroGeral = null) }
    }

    fun onSenhaChange(senha: String) {
        _uiState.update { it.copy(senha = senha, erros = it.erros.copy(senha = null), erroGeral = null) }
    }

    fun onConfirmacaoChange(confirmacao: String) {
        _uiState.update {
            it.copy(confirmacao = confirmacao, erros = it.erros.copy(confirmacao = null), erroGeral = null)
        }
    }

    fun enviar() {
        val estado = _uiState.value
        if (estado.carregando) return
        when (estado.modo) {
            ModoAuth.ENTRAR -> entrar(estado)
            ModoAuth.CRIAR_CONTA -> criarConta(estado)
        }
    }

    fun esqueciSenha() {
        val estado = _uiState.value
        if (estado.carregando) return
        val erroEmail = validarEmail(estado.email)
        if (erroEmail != null) {
            _uiState.update { it.copy(erros = ErrosAuth(email = erroEmail), erroGeral = null) }
            return
        }
        val email = estado.email.trim()
        executar(
            operacao = { authRepository.enviarRedefinicaoSenha(email) },
            aoConcluir = {
                _uiState.update {
                    it.copy(mensagemInfo = "Enviamos um link de redefinição de senha para $email.")
                }
            }
        )
    }

    fun mensagemInfoMostrada() {
        _uiState.update { it.copy(mensagemInfo = null) }
    }

    private fun entrar(estado: AuthUiState) {
        val erros = validarEntrada(estado.email, estado.senha)
        if (erros.temErro) {
            _uiState.update { it.copy(erros = erros) }
            return
        }
        executar(
            operacao = { authRepository.entrar(estado.email.trim(), estado.senha) },
            aoConcluir = { _uiState.update { it.copy(autenticado = true) } }
        )
    }

    private fun criarConta(estado: AuthUiState) {
        val erros = validarCadastro(estado.nome, estado.email, estado.senha, estado.confirmacao)
        if (erros.temErro) {
            _uiState.update { it.copy(erros = erros) }
            return
        }
        executar(
            operacao = {
                authRepository.criarConta(estado.nome.trim(), estado.email.trim(), estado.senha)
            },
            aoConcluir = { _uiState.update { it.copy(autenticado = true) } }
        )
    }

    private fun <T> executar(operacao: suspend () -> Result<T>, aoConcluir: (T) -> Unit) {
        _uiState.update { it.copy(carregando = true, erros = ErrosAuth(), erroGeral = null) }
        viewModelScope.launch {
            operacao()
                .onSuccess(aoConcluir)
                .onFailure { erro -> _uiState.update { it.copy(erroGeral = erro.message) } }
            _uiState.update { it.copy(carregando = false) }
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as HabitosNowApp
                AuthViewModel(app.container.authRepository)
            }
        }
    }
}
