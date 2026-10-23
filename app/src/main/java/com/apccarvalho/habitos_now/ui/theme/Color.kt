package com.apccarvalho.habitos_now.ui.theme

import androidx.compose.ui.graphics.Color

// Paleta do protótipo no Figma
val Primaria = Color(0xFF006666)
val PrimariaPressionada = Color(0xFF004D4D)
val Destaque = Color(0xFF00FFFF)
val Fundo = Color(0xFFFFFFFF)
val Neutro = Color(0xFFF1F4F4)        // campos, contêiner de abas
val Borda = Color(0xFFD5DCDC)
val Texto = Color(0xFF1F2626)
val Superficie = Color(0xFFE2FFFF)    // card de resumo, card de hábito feito
val AcentoSuave = Color(0xFFFDEBD3)   // selo de sequência
val AcentoTexto = Color(0xFF8A4B0F)
val Erro = Color(0xFFC0392B)

/**
 * Cores da paleta que não têm papel direto no ColorScheme do Material 3.
 * Uso nas telas: HabitosNowColors.Neutro, HabitosNowColors.Superficie etc.
 */
object HabitosNowColors {
    val Neutro = com.apccarvalho.habitos_now.ui.theme.Neutro
    val Borda = com.apccarvalho.habitos_now.ui.theme.Borda
    val Superficie = com.apccarvalho.habitos_now.ui.theme.Superficie
    val AcentoSuave = com.apccarvalho.habitos_now.ui.theme.AcentoSuave
    val AcentoTexto = com.apccarvalho.habitos_now.ui.theme.AcentoTexto
    val Destaque = com.apccarvalho.habitos_now.ui.theme.Destaque
    val PrimariaPressionada = com.apccarvalho.habitos_now.ui.theme.PrimariaPressionada
}
