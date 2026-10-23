package com.apccarvalho.habitos_now.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// Tela temporária da etapa 1, só para conferir tema e paleta.
// Sai da MainActivity quando a navegação entrar.
private val paleta = listOf(
    Primaria, PrimariaPressionada, Destaque, Fundo, Neutro, Borda,
    Texto, Superficie, AcentoSuave, AcentoTexto, Erro
)

@Composable
fun VerificacaoTema(modifier: Modifier = Modifier) {
    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Hábitos Now",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Primaria
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                paleta.forEach { cor -> AmostraCor(cor) }
            }
        }
    }
}

@Composable
private fun AmostraCor(cor: Color) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .background(cor, MaterialTheme.shapes.small)
            .border(1.dp, Borda, MaterialTheme.shapes.small)
    )
}

@Preview(showBackground = true)
@Composable
private fun VerificacaoTemaPreview() {
    HabitosNowTheme {
        VerificacaoTema()
    }
}
