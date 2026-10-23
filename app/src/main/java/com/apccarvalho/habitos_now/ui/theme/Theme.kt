package com.apccarvalho.habitos_now.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Primaria,
    onPrimary = Color.White,
    primaryContainer = Superficie,
    onPrimaryContainer = PrimariaPressionada,
    secondary = PrimariaPressionada,
    onSecondary = Color.White,
    secondaryContainer = Superficie,
    onSecondaryContainer = Texto,
    tertiary = AcentoTexto,
    onTertiary = Color.White,
    tertiaryContainer = AcentoSuave,
    onTertiaryContainer = AcentoTexto,
    background = Fundo,
    onBackground = Texto,
    surface = Fundo,
    onSurface = Texto,
    surfaceVariant = Neutro,
    onSurfaceVariant = Texto,
    surfaceTint = Primaria,
    surfaceBright = Fundo,
    surfaceDim = Borda,
    surfaceContainerLowest = Fundo,
    surfaceContainerLow = Fundo,
    surfaceContainer = Neutro,
    surfaceContainerHigh = Neutro,
    surfaceContainerHighest = Neutro,
    outline = Borda,
    outlineVariant = Borda,
    error = Erro,
    onError = Color.White
)

@Composable
fun HabitosNowTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
