package com.apccarvalho.habitos_now.data.habit

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.apccarvalho.habitos_now.data.model.Habit
import java.io.IOException
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

val Context.habitsDataStore: DataStore<Preferences> by preferencesDataStore(name = "habitos")

class DataStoreHabitRepository(
    private val dataStore: DataStore<Preferences>
) : HabitRepository {

    private val json = Json { ignoreUnknownKeys = true }

    override fun observarHabitos(uid: String): Flow<List<Habit>> =
        dataStore.data
            .catch { erro ->
                if (erro is IOException) emit(emptyPreferences()) else throw erro
            }
            .map { prefs -> decodificar(prefs[chave(uid)]) }

    override suspend fun buscar(uid: String, id: String): Habit? =
        observarHabitos(uid).first().find { it.id == id }

    override suspend fun salvar(uid: String, habit: Habit) = atualizarLista(uid) { habitos ->
        if (habitos.any { it.id == habit.id }) {
            habitos.map { if (it.id == habit.id) habit else it }
        } else {
            habitos + habit
        }
    }

    override suspend fun excluir(uid: String, id: String) = atualizarLista(uid) { habitos ->
        habitos.filterNot { it.id == id }
    }

    override suspend fun alternarHoje(uid: String, id: String, hoje: LocalDate) =
        atualizarLista(uid) { habitos ->
            val dataHoje = hoje.toString()
            habitos.map { habit ->
                if (habit.id != id) {
                    habit
                } else if (dataHoje in habit.datasConcluidas) {
                    habit.copy(datasConcluidas = habit.datasConcluidas - dataHoje)
                } else {
                    habit.copy(datasConcluidas = habit.datasConcluidas + dataHoje)
                }
            }
        }

    private suspend fun atualizarLista(uid: String, transformar: (List<Habit>) -> List<Habit>) {
        dataStore.edit { prefs ->
            val chave = chave(uid)
            prefs[chave] = json.encodeToString(transformar(decodificar(prefs[chave])))
        }
    }

    private fun decodificar(conteudo: String?): List<Habit> {
        if (conteudo == null) return emptyList()
        return try {
            json.decodeFromString<List<Habit>>(conteudo)
        } catch (e: SerializationException) {
            emptyList()
        }
    }

    private fun chave(uid: String) = stringPreferencesKey("habits_$uid")
}
