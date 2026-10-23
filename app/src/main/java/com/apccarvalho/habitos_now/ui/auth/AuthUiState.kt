package com.apccarvalho.habitos_now.ui.auth

enum class ModoAuth { ENTRAR, CRIAR_CONTA }

data class AuthUiState(
    val modo: ModoAuth = ModoAuth.ENTRAR,
    val nome: String = "",
    val email: String = "",
    val senha: String = "",
    val confirmacao: String = "",
    val erros: ErrosAuth = ErrosAuth(),
    val carregando: Boolean = false,
    val erroGeral: String? = null,
    val mensagemInfo: String? = null,
    val autenticado: Boolean = false
)
