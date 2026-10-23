package com.apccarvalho.habitos_now.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.apccarvalho.habitos_now.ui.components.BotaoPrimario
import com.apccarvalho.habitos_now.ui.components.CampoTexto
import com.apccarvalho.habitos_now.ui.theme.HabitosNowColors
import com.apccarvalho.habitos_now.ui.theme.HabitosNowTheme

@Composable
fun AuthScreen(
    onAutenticado: () -> Unit,
    viewModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.autenticado) {
        if (uiState.autenticado) onAutenticado()
    }
    LaunchedEffect(uiState.mensagemInfo) {
        uiState.mensagemInfo?.let { mensagem ->
            snackbarHostState.showSnackbar(mensagem)
            viewModel.mensagemInfoMostrada()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        AuthConteudo(
            uiState = uiState,
            innerPadding = innerPadding,
            onTrocarModo = viewModel::trocarModo,
            onNomeChange = viewModel::onNomeChange,
            onEmailChange = viewModel::onEmailChange,
            onSenhaChange = viewModel::onSenhaChange,
            onConfirmacaoChange = viewModel::onConfirmacaoChange,
            onEnviar = viewModel::enviar,
            onEsqueciSenha = viewModel::esqueciSenha
        )
    }
}

@Composable
private fun AuthConteudo(
    uiState: AuthUiState,
    innerPadding: PaddingValues,
    onTrocarModo: (ModoAuth) -> Unit,
    onNomeChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onSenhaChange: (String) -> Unit,
    onConfirmacaoChange: (String) -> Unit,
    onEnviar: () -> Unit,
    onEsqueciSenha: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val aoConcluirTeclado = KeyboardActions(onDone = {
        focusManager.clearFocus()
        onEnviar()
    })
    val criandoConta = uiState.modo == ModoAuth.CRIAR_CONTA

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .consumeWindowInsets(innerPadding)
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(start = 24.dp, end = 24.dp, top = 48.dp, bottom = 32.dp)
    ) {
        Cabecalho()
        Spacer(Modifier.height(40.dp))
        AbasAuth(modo = uiState.modo, onTrocarModo = onTrocarModo)
        Spacer(Modifier.height(28.dp))

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            if (criandoConta) {
                CampoTexto(
                    rotulo = "Nome",
                    valor = uiState.nome,
                    onValorChange = onNomeChange,
                    placeholder = "Como podemos te chamar?",
                    erro = uiState.erros.nome,
                    capitalization = KeyboardCapitalization.Words,
                    habilitado = !uiState.carregando
                )
            }
            CampoTexto(
                rotulo = "E-mail",
                valor = uiState.email,
                onValorChange = onEmailChange,
                placeholder = "seu@email.com",
                erro = uiState.erros.email,
                keyboardType = KeyboardType.Email,
                habilitado = !uiState.carregando
            )
            CampoTexto(
                rotulo = "Senha",
                valor = uiState.senha,
                onValorChange = onSenhaChange,
                placeholder = "••••••••",
                erro = uiState.erros.senha,
                dica = if (criandoConta) "Mínimo de $TAMANHO_MINIMO_SENHA caracteres" else null,
                senha = true,
                keyboardType = KeyboardType.Password,
                imeAction = if (criandoConta) ImeAction.Next else ImeAction.Done,
                keyboardActions = if (criandoConta) KeyboardActions.Default else aoConcluirTeclado,
                habilitado = !uiState.carregando
            )
            if (criandoConta) {
                CampoTexto(
                    rotulo = "Confirmar senha",
                    valor = uiState.confirmacao,
                    onValorChange = onConfirmacaoChange,
                    placeholder = "••••••••",
                    erro = uiState.erros.confirmacao,
                    senha = true,
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                    keyboardActions = aoConcluirTeclado,
                    habilitado = !uiState.carregando
                )
            }
        }

        Spacer(Modifier.height(28.dp))
        uiState.erroGeral?.let { erro ->
            Text(
                text = erro,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )
        }
        BotaoPrimario(
            texto = if (criandoConta) "Criar conta" else "Entrar",
            onClick = {
                focusManager.clearFocus()
                onEnviar()
            },
            carregando = uiState.carregando
        )

        if (!criandoConta) {
            Spacer(Modifier.height(12.dp))
            TextButton(onClick = onEsqueciSenha, enabled = !uiState.carregando) {
                Text(
                    text = "Esqueci minha senha",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun Cabecalho() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(72.dp)
                .background(MaterialTheme.colorScheme.primary, CircleShape)
        ) {
            Icon(
                imageVector = Icons.Rounded.Check,
                contentDescription = null,
                tint = HabitosNowColors.Destaque,
                modifier = Modifier.size(36.dp)
            )
        }
        Spacer(Modifier.height(24.dp))
        Text(
            text = "Hábitos Now",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Pequenos passos, todo dia",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
        )
    }
}

@Composable
private fun AbasAuth(modo: ModoAuth, onTrocarModo: (ModoAuth) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(HabitosNowColors.Neutro, MaterialTheme.shapes.medium)
            .padding(4.dp)
            .selectableGroup()
    ) {
        Aba(texto = "Entrar", ativa = modo == ModoAuth.ENTRAR, onClick = { onTrocarModo(ModoAuth.ENTRAR) })
        Aba(
            texto = "Criar conta",
            ativa = modo == ModoAuth.CRIAR_CONTA,
            onClick = { onTrocarModo(ModoAuth.CRIAR_CONTA) }
        )
    }
}

@Composable
private fun RowScope.Aba(
    texto: String,
    ativa: Boolean,
    onClick: () -> Unit
) {
    val cores = MaterialTheme.colorScheme
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .weight(1f)
            .height(40.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(if (ativa) cores.primary else Color.Transparent)
            .selectable(selected = ativa, role = Role.Tab, onClick = onClick)
    ) {
        Text(
            text = texto,
            style = MaterialTheme.typography.labelLarge,
            color = if (ativa) cores.onPrimary else cores.onSurface
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun AuthEntrarPreview() {
    HabitosNowTheme {
        AuthConteudo(
            uiState = AuthUiState(),
            innerPadding = PaddingValues(),
            onTrocarModo = {}, onNomeChange = {}, onEmailChange = {}, onSenhaChange = {},
            onConfirmacaoChange = {}, onEnviar = {}, onEsqueciSenha = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun AuthCriarContaPreview() {
    HabitosNowTheme {
        AuthConteudo(
            uiState = AuthUiState(
                modo = ModoAuth.CRIAR_CONTA,
                erros = ErrosAuth(confirmacao = "As senhas não coincidem.")
            ),
            innerPadding = PaddingValues(),
            onTrocarModo = {}, onNomeChange = {}, onEmailChange = {}, onSenhaChange = {},
            onConfirmacaoChange = {}, onEnviar = {}, onEsqueciSenha = {}
        )
    }
}
