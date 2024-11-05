package br.com.steventung.movinlist.ui.dialogs.deleteaccountdialog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import br.com.steventung.mobilist.R

@Composable
fun DeleteAccountDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {},
    onConfirm: () -> Unit = {},
    onChangePasswordVisibility: (Boolean) -> Unit = {},
    state: DeleteAccountDialogUiState
) {
    Dialog(onDismissRequest = onDismiss) {
        Column {
            AnimatedVisibility(!state.errorMessage.isNullOrBlank()) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.Red)) {
                    state.errorMessage?.let {
                        Text(
                            text = it,
                            modifier = Modifier.padding(8.dp),
                            color = Color.White
                        )
                    }
                }
            }
            Column(
                modifier = modifier
                    .background(color = Color.White)
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (state.isDeleteLoading) {
                    Column(
                        modifier = Modifier.size(300.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                        Text(
                            text = "Deletando usuário...",
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                } else {
                    Text(
                        text = "Deseja deletar sua conta?",
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally),
                        fontSize = 18.sp,
                        color = Color.DarkGray
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Insira a senha para prosseguir com a remoção da conta:",
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally),
                        color = Color.DarkGray
                    )
                    TextField(
                        value = state.email,
                        onValueChange = {},
                        modifier = Modifier.fillMaxWidth(),
                        enabled = false,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "ícone email"
                            )
                        },
                        label = { Text(text = "E-mail") },
                        maxLines = 1
                    )
                    TextField(
                        value = state.password,
                        onValueChange = { state.onPasswordChanged(it) },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "ícone senha"
                            )
                        },
                        trailingIcon = {
                            if (state.password.isNotEmpty()) {
                                Icon(
                                    painter = if (state.passwordVisibility) {
                                        painterResource(R.drawable.ic_action_visible)
                                    } else {
                                        painterResource(R.drawable.ic_action_visibility_off)
                                    },
                                    contentDescription = "icone password visible",
                                    modifier = Modifier.clickable {
                                        onChangePasswordVisibility(state.passwordVisibility)
                                    },
                                    tint = Color.DarkGray
                                )
                            }
                        },
                        label = { Text(text = "Senha") },
                        maxLines = 1,
                        visualTransformation = if (state.passwordVisibility) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        }
                    )
                    Text(
                        text = "Ao confirmar, sua conta e todas as informações contidas nela serão " +
                                "removidas permanentemente.",
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.DarkGray
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text(text = "Cancel")
                        }
                        TextButton(onClick = onConfirm) {
                            Text(text = "Confirm")
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun DeleteAccountDialogPreview() {
    DeleteAccountDialog(
        state = DeleteAccountDialogUiState(
            email = "steven@movinlist.com",
            password = "aaaaa"
        )
    )
}

@Preview
@Composable
private fun DeleteAccountDialogWithErrorPreview() {
    DeleteAccountDialog(
        state = DeleteAccountDialogUiState(
            email = "steven@movinlist.com",
            password = "aaaaa",
            errorMessage = "Erro: Senha incorreta"
        )
    )
}

@Preview
@Composable
private fun DeleteAccountDialogLoadingPreview() {
    DeleteAccountDialog(
        state = DeleteAccountDialogUiState(
            isDeleteLoading = true
        )
    )
}