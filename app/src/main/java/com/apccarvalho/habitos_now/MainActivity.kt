package com.apccarvalho.habitos_now

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.apccarvalho.habitos_now.ui.navigation.AppNavHost
import com.apccarvalho.habitos_now.ui.theme.HabitosNowTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val container = (application as HabitosNowApp).container
        setContent {
            HabitosNowTheme {
                AppNavHost(authRepository = container.authRepository)
            }
        }
    }
}
