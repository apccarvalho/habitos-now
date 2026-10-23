package com.apccarvalho.habitos_now.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Habit(
    val id: String,
    val emoji: String,
    val nome: String,
    val descricao: String = "",
    val criadoEm: String,
    val datasConcluidas: Set<String> = emptySet()
)
