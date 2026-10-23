package com.apccarvalho.habitos_now

import android.content.Context
import com.apccarvalho.habitos_now.data.auth.AuthRepository
import com.apccarvalho.habitos_now.data.auth.FirebaseAuthRepository
import com.apccarvalho.habitos_now.data.habit.DataStoreHabitRepository
import com.apccarvalho.habitos_now.data.habit.HabitRepository
import com.apccarvalho.habitos_now.data.habit.habitsDataStore

class AppContainer(context: Context) {
    val authRepository: AuthRepository = FirebaseAuthRepository()
    val habitRepository: HabitRepository = DataStoreHabitRepository(context.habitsDataStore)
}
