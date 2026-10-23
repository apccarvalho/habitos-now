package com.apccarvalho.habitos_now.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apccarvalho.habitos_now.ui.theme.HabitosNowColors
import com.apccarvalho.habitos_now.ui.theme.HabitosNowTheme

@Composable
fun BotaoPrimario(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    carregando: Boolean = false,
    habilitado: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressionado by interactionSource.collectIsPressedAsState()
    val cores = MaterialTheme.colorScheme

    Button(
        onClick = onClick,
        enabled = habilitado && !carregando,
        shape = MaterialTheme.shapes.medium,
        interactionSource = interactionSource,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (pressionado) HabitosNowColors.PrimariaPressionada else cores.primary,
            contentColor = cores.onPrimary,
            disabledContainerColor = if (carregando) cores.primary else cores.primary.copy(alpha = 0.4f),
            disabledContentColor = cores.onPrimary
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        if (carregando) {
            CircularProgressIndicator(
                color = cores.onPrimary,
                strokeWidth = 2.5.dp,
                modifier = Modifier.size(24.dp)
            )
        } else {
            Text(text = texto, style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BotaoPrimarioPreview() {
    HabitosNowTheme {
        BotaoPrimario(texto = "Entrar", onClick = {}, modifier = Modifier.padding(24.dp))
    }
}
