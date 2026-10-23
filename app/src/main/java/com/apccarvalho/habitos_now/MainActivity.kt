package com.apccarvalho.habitos_now

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.apccarvalho.habitos_now.ui.theme.HabitosNowTheme
import com.apccarvalho.habitos_now.ui.theme.VerificacaoTema

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HabitosNowTheme {
                VerificacaoTema()
            }
        }
    }
}
