package com.apccarvalho.habitos_now.data.model

import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class HabitStatsTest {

    private val hoje = LocalDate.of(2026, 10, 23)

    private fun habito(
        criadoEm: LocalDate = hoje.minusDays(30),
        vararg diasAtras: Long
    ) = Habit(
        id = "1",
        emoji = "💧",
        nome = "Beber água",
        criadoEm = criadoEm.toString(),
        datasConcluidas = diasAtras.map { hoje.minusDays(it).toString() }.toSet()
    )

    @Test
    fun `habito sem datas concluidas zera todas as estatisticas`() {
        val habit = habito()

        assertFalse(feitoHoje(habit, hoje))
        assertEquals(0, sequenciaAtual(habit, hoje))
        assertEquals(0, recorde(habit))
        assertEquals(0, totalDias(habit))
    }

    @Test
    fun `sequencia conta ate hoje quando feito hoje`() {
        val habit = habito(diasAtras = longArrayOf(0, 1, 2))

        assertTrue(feitoHoje(habit, hoje))
        assertEquals(3, sequenciaAtual(habit, hoje))
    }

    @Test
    fun `sequencia conta ate ontem quando ainda nao feito hoje`() {
        val habit = habito(diasAtras = longArrayOf(1, 2, 3, 4))

        assertFalse(feitoHoje(habit, hoje))
        assertEquals(4, sequenciaAtual(habit, hoje))
    }

    @Test
    fun `sequencia e quebrada por um dia faltando`() {
        val habit = habito(diasAtras = longArrayOf(0, 1, 3, 4, 5))

        assertEquals(2, sequenciaAtual(habit, hoje))
    }

    @Test
    fun `sequencia zera quando nao feito hoje nem ontem`() {
        val habit = habito(diasAtras = longArrayOf(2, 3))

        assertEquals(0, sequenciaAtual(habit, hoje))
    }

    @Test
    fun `recorde maior que a sequencia atual`() {
        val habit = habito(diasAtras = longArrayOf(0, 1, 5, 6, 7, 8, 9))

        assertEquals(2, sequenciaAtual(habit, hoje))
        assertEquals(5, recorde(habit))
        assertEquals(7, totalDias(habit))
    }

    @Test
    fun `ultimos7Dias marca dias antes da criacao`() {
        val habit = habito(criadoEm = hoje.minusDays(3), diasAtras = longArrayOf(0, 2))

        val dias = ultimos7Dias(habit, hoje)

        assertEquals((6L downTo 0L).map { hoje.minusDays(it) }, dias.map { it.data })
        assertEquals(
            listOf(
                StatusDia.ANTES_DA_CRIACAO,
                StatusDia.ANTES_DA_CRIACAO,
                StatusDia.ANTES_DA_CRIACAO,
                StatusDia.NAO_FEITO,
                StatusDia.FEITO,
                StatusDia.NAO_FEITO,
                StatusDia.FEITO
            ),
            dias.map { it.status }
        )
    }
}
