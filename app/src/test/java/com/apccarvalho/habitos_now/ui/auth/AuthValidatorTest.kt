package com.apccarvalho.habitos_now.ui.auth

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AuthValidatorTest {

    @Test
    fun `email vazio ou so com espacos e obrigatorio`() {
        assertEquals("Informe seu e-mail.", validarEmail(""))
        assertEquals("Informe seu e-mail.", validarEmail("   "))
    }

    @Test
    fun `email em formato invalido`() {
        listOf("ana", "ana@", "ana@email", "@email.com", "ana email@x.com").forEach { email ->
            assertEquals(email, "E-mail inválido.", validarEmail(email))
        }
    }

    @Test
    fun `email valido aceita espacos nas pontas`() {
        assertNull(validarEmail("ana@email.com"))
        assertNull(validarEmail("  ana.silva+teste@email.com.br  "))
    }

    @Test
    fun `senha vazia ou com menos de 6 caracteres`() {
        assertEquals("Informe sua senha.", validarSenha(""))
        assertEquals("A senha deve ter no mínimo 6 caracteres.", validarSenha("12345"))
        assertNull(validarSenha("123456"))
    }

    @Test
    fun `confirmacao deve ser igual a senha`() {
        assertEquals("Confirme sua senha.", validarConfirmacao("123456", ""))
        assertEquals("As senhas não coincidem.", validarConfirmacao("123456", "123457"))
        assertNull(validarConfirmacao("123456", "123456"))
    }

    @Test
    fun `nome e obrigatorio ignorando espacos`() {
        assertEquals("Informe seu nome.", validarNome(""))
        assertEquals("Informe seu nome.", validarNome("   "))
        assertNull(validarNome(" Ana "))
    }

    @Test
    fun `validarEntrada nao exige nome nem confirmacao`() {
        val erros = validarEntrada("ana@email.com", "123456")

        assertFalse(erros.temErro)
    }

    @Test
    fun `validarCadastro aponta erro em cada campo`() {
        val erros = validarCadastro(nome = " ", email = "ana", senha = "123", confirmacao = "321")

        assertTrue(erros.temErro)
        assertEquals("Informe seu nome.", erros.nome)
        assertEquals("E-mail inválido.", erros.email)
        assertEquals("A senha deve ter no mínimo 6 caracteres.", erros.senha)
        assertEquals("As senhas não coincidem.", erros.confirmacao)
    }

    @Test
    fun `validarCadastro sem erros quando tudo esta preenchido`() {
        val erros = validarCadastro("Ana", "ana@email.com", "123456", "123456")

        assertFalse(erros.temErro)
    }
}
