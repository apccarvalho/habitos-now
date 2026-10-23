package com.apccarvalho.habitos_now.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.apccarvalho.habitos_now.data.auth.AuthRepository
import com.apccarvalho.habitos_now.ui.auth.AuthScreen
import com.apccarvalho.habitos_now.ui.components.BotaoPrimario

object Rotas {
    const val AUTH = "auth"
    const val HABITS = "habits"
}

@Composable
fun AppNavHost(authRepository: AuthRepository) {
    val navController = rememberNavController()
    val destinoInicial = remember {
        if (authRepository.usuarioAtual != null) Rotas.HABITS else Rotas.AUTH
    }

    NavHost(navController = navController, startDestination = destinoInicial) {
        composable(Rotas.AUTH) {
            AuthScreen(
                onAutenticado = {
                    navController.navigate(Rotas.HABITS) {
                        popUpTo(Rotas.AUTH) { inclusive = true }
                    }
                }
            )
        }
        composable(Rotas.HABITS) {
            val nome = remember { authRepository.usuarioAtual?.nome.orEmpty() }
            HabitsProvisorio(
                nome = nome,
                onSair = {
                    authRepository.sair()
                    navController.navigate(Rotas.AUTH) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                }
            )
        }
    }
}

@Composable
private fun HabitsProvisorio(nome: String, onSair: () -> Unit) {
    Scaffold { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp)
        ) {
            Text(
                text = "Olá, $nome",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
            BotaoPrimario(texto = "Sair", onClick = onSair)
        }
    }
}
