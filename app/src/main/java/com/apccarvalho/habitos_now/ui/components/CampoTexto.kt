package com.apccarvalho.habitos_now.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apccarvalho.habitos_now.ui.theme.HabitosNowColors
import com.apccarvalho.habitos_now.ui.theme.HabitosNowTheme

@Composable
fun CampoTexto(
    rotulo: String,
    valor: String,
    onValorChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    erro: String? = null,
    dica: String? = null,
    senha: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.None,
    imeAction: ImeAction = ImeAction.Next,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    habilitado: Boolean = true
) {
    var focado by remember { mutableStateOf(false) }
    var senhaVisivel by rememberSaveable { mutableStateOf(false) }
    val cores = MaterialTheme.colorScheme
    val formato = MaterialTheme.shapes.small
    val corBorda = when {
        erro != null -> cores.error
        focado -> cores.primary
        else -> HabitosNowColors.Borda
    }
    val larguraBorda = if (erro != null || focado) 2.dp else 1.dp
    val estiloTexto = MaterialTheme.typography.bodyLarge.copy(color = cores.onSurface)

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(text = rotulo, style = MaterialTheme.typography.labelLarge, color = cores.onSurface)
        BasicTextField(
            value = valor,
            onValueChange = onValorChange,
            enabled = habilitado,
            singleLine = true,
            textStyle = estiloTexto,
            cursorBrush = SolidColor(cores.primary),
            visualTransformation = if (senha && !senhaVisivel) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                capitalization = capitalization,
                imeAction = imeAction
            ),
            keyboardActions = keyboardActions,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focado = it.isFocused },
            decorationBox = { campo ->
                Row(
                    modifier = Modifier
                        .height(52.dp)
                        .background(HabitosNowColors.Neutro, formato)
                        .border(larguraBorda, corBorda, formato)
                        .padding(start = 16.dp, end = if (senha) 4.dp else 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        if (valor.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = estiloTexto,
                                color = cores.onSurface.copy(alpha = 0.5f)
                            )
                        }
                        campo()
                    }
                    if (senha) {
                        IconButton(onClick = { senhaVisivel = !senhaVisivel }) {
                            Icon(
                                imageVector = if (senhaVisivel) {
                                    Icons.Outlined.VisibilityOff
                                } else {
                                    Icons.Outlined.Visibility
                                },
                                contentDescription = if (senhaVisivel) "Ocultar senha" else "Mostrar senha",
                                tint = cores.primary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        )
        when {
            erro != null -> Text(
                text = erro,
                style = MaterialTheme.typography.bodySmall,
                color = cores.error
            )
            dica != null -> Text(
                text = dica,
                style = MaterialTheme.typography.bodySmall,
                color = cores.onSurface
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CampoTextoPreview() {
    HabitosNowTheme {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CampoTexto(rotulo = "E-mail", valor = "", onValorChange = {}, placeholder = "seu@email.com")
            CampoTexto(
                rotulo = "Senha",
                valor = "123456",
                onValorChange = {},
                senha = true,
                dica = "Mínimo de 6 caracteres"
            )
            CampoTexto(rotulo = "Nome", valor = "", onValorChange = {}, erro = "Informe seu nome.")
        }
    }
}
