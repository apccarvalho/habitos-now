package com.apccarvalho.habitos_now.ui.auth

const val TAMANHO_MINIMO_SENHA = 6

private val formatoEmail = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

data class ErrosAuth(
    val nome: String? = null,
    val email: String? = null,
    val senha: String? = null,
    val confirmacao: String? = null
) {
    val temErro: Boolean
        get() = listOf(nome, email, senha, confirmacao).any { it != null }
}

fun validarNome(nome: String): String? =
    if (nome.isBlank()) "Informe seu nome." else null

fun validarEmail(email: String): String? = when {
    email.isBlank() -> "Informe seu e-mail."
    !formatoEmail.matches(email.trim()) -> "E-mail inválido."
    else -> null
}

fun validarSenha(senha: String): String? = when {
    senha.isEmpty() -> "Informe sua senha."
    senha.length < TAMANHO_MINIMO_SENHA ->
        "A senha deve ter no mínimo $TAMANHO_MINIMO_SENHA caracteres."
    else -> null
}

fun validarConfirmacao(senha: String, confirmacao: String): String? = when {
    confirmacao.isEmpty() -> "Confirme sua senha."
    confirmacao != senha -> "As senhas não coincidem."
    else -> null
}

fun validarEntrada(email: String, senha: String) = ErrosAuth(
    email = validarEmail(email),
    senha = validarSenha(senha)
)

fun validarCadastro(nome: String, email: String, senha: String, confirmacao: String) = ErrosAuth(
    nome = validarNome(nome),
    email = validarEmail(email),
    senha = validarSenha(senha),
    confirmacao = validarConfirmacao(senha, confirmacao)
)
