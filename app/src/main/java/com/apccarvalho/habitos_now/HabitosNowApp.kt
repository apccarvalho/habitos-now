package com.apccarvalho.habitos_now

import android.app.Application

class HabitosNowApp : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer()
    }
}
