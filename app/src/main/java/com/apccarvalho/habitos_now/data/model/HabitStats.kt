package com.apccarvalho.habitos_now.data.model

import java.time.LocalDate
import java.time.format.DateTimeParseException

enum class StatusDia { FEITO, NAO_FEITO, ANTES_DA_CRIACAO }

data class DiaStatus(val data: LocalDate, val status: StatusDia)

fun feitoHoje(habit: Habit, hoje: LocalDate): Boolean =
    hoje.toString() in habit.datasConcluidas

// Se ainda não foi feito hoje, a sequência conta até ontem: assim ela não
// zera de manhã antes de a pessoa ter a chance de marcar o dia.
fun sequenciaAtual(habit: Habit, hoje: LocalDate): Int {
    val datas = habit.datasConcluidasComoLocalDate()
    var dia = if (feitoHoje(habit, hoje)) hoje else hoje.minusDays(1)
    var sequencia = 0
    while (dia in datas) {
        sequencia++
        dia = dia.minusDays(1)
    }
    return sequencia
}

fun recorde(habit: Habit): Int {
    var recorde = 0
    var sequencia = 0
    var anterior: LocalDate? = null
    for (dia in habit.datasConcluidasComoLocalDate().sorted()) {
        sequencia = if (anterior != null && dia == anterior.plusDays(1)) sequencia + 1 else 1
        recorde = maxOf(recorde, sequencia)
        anterior = dia
    }
    return recorde
}

fun totalDias(habit: Habit): Int = habit.datasConcluidasComoLocalDate().size

fun ultimos7Dias(habit: Habit, hoje: LocalDate): List<DiaStatus> {
    val criadoEm = parseData(habit.criadoEm)
    val datas = habit.datasConcluidasComoLocalDate()
    return (6L downTo 0L).map { diasAtras ->
        val dia = hoje.minusDays(diasAtras)
        val status = when {
            criadoEm != null && dia < criadoEm -> StatusDia.ANTES_DA_CRIACAO
            dia in datas -> StatusDia.FEITO
            else -> StatusDia.NAO_FEITO
        }
        DiaStatus(dia, status)
    }
}

private fun Habit.datasConcluidasComoLocalDate(): Set<LocalDate> =
    datasConcluidas.mapNotNullTo(mutableSetOf(), ::parseData)

private fun parseData(iso: String): LocalDate? =
    try {
        LocalDate.parse(iso)
    } catch (e: DateTimeParseException) {
        null
    }
