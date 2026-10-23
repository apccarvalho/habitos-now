package com.apccarvalho.habitos_now.data.habit

import com.apccarvalho.habitos_now.data.model.Habit
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow

interface HabitRepository {
    fun observarHabitos(uid: String): Flow<List<Habit>>
    suspend fun buscar(uid: String, id: String): Habit?
    suspend fun salvar(uid: String, habit: Habit)
    suspend fun excluir(uid: String, id: String)
    suspend fun alternarHoje(uid: String, id: String, hoje: LocalDate)
}
