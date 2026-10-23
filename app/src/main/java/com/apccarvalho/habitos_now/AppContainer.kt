package com.apccarvalho.habitos_now

import android.content.Context
import com.apccarvalho.habitos_now.data.habit.DataStoreHabitRepository
import com.apccarvalho.habitos_now.data.habit.HabitRepository
import com.apccarvalho.habitos_now.data.habit.habitsDataStore

class AppContainer(context: Context) {
    val habitRepository: HabitRepository = DataStoreHabitRepository(context.habitsDataStore)
}
