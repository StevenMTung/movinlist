package br.com.steventung.movinlist.ui.dialogs.passwordformdialog

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import br.com.steventung.mobilist.R

@Composable
fun PasswordFormDialog(
    modifier: Modifier = Modifier,
    state: PasswordFormDialogUiState,
    onDismiss: () -> Unit = {},
    onConfirm: () -> Unit = {},
    onChangeCurrentPasswordVisibility: (Boolean) -> Unit = {},
    onChangeNewPasswordVisibility: (Boolean) -> Unit = {},
    onChangeNewPasswordVerificationVisibility: (Boolean) -> Unit = {}
) {
    Dialog(onDismissRequest = onDismiss) {
        if (state.isLoading) {
            Column(
                modifier = Modifier
                    .height(300.dp)
                    .width(300.dp)
                    .background(color = Color.White),
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                Text(text = "Carregando...", Modifier.align(Alignment.CenterHorizontally))
            }
        } else {
            Column(modifier = modifier) {
                AnimatedVisibility(!state.passwordErrorMessage.isNullOrBlank()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.Red)
                    ) {
                        state.passwordErrorMessage?.let {
                            Text(
                                text = it,
                                color = Color.White,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .background(color = Color.White, shape = RoundedCornerShape(10.dp))
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Redefinir Senha",
                        modifier = Modifier
                            .fillMaxWidth(),
                        fontSize = 24.sp,
                        color = Color.DarkGray,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(8.dp))
                    TextField(
                        value = state.currentPassword,
                        onValueChange = { state.onCurrentPasswordChanged(it) },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text(text = "Senha Atual")
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        ),
                        maxLines = 1,
                        trailingIcon = {
                            if (state.currentPassword.isNotEmpty()) {
                                Icon(
                                    painter = if (state.isShowCurrentPassword) {
                                        painterResource(R.drawable.ic_action_visible)
                                    } else {
                                        painterResource(R.drawable.ic_action_visibility_off)
                                    },
                                    contentDescription = "icone password visible",
                                    modifier = Modifier.clickable {
                                        onChangeCurrentPasswordVisibility(state.isShowCurrentPassword)
                                    },
                                    tint = Color.DarkGray
                                )
                            }
                        },
                        visualTransformation = if (state.isShowCurrentPassword) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        }
                    )
                    TextField(
                        value = state.newPassword,
                        onValueChange = { state.onNewPasswordChanged(it) },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text(text = "Senha Nova")
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        ),
                        maxLines = 1,
                        trailingIcon = {
                            if (state.newPassword.isNotEmpty()) {
                                Icon(
                                    painter = if (state.isShowNewPassword) {
                                        painterResource(R.drawable.ic_action_visible)
                                    } else {
                                        painterResource(R.drawable.ic_action_visibility_off)
                                    },
                                    contentDescription = "icone password visible",
                                    modifier = Modifier.clickable {
                                        onChangeNewPasswordVisibility(state.isShowNewPassword)
                                    },
                                    tint = Color.DarkGray
                                )
                            }
                        },
                        visualTransformation = if (state.isShowNewPassword) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        }
                    )
                    AnimatedVisibility(!state.newPasswordVerificationError.isNullOrBlank()) {
                        state.newPasswordVerificationError?.let {
                            Log.i("MensagemErro", "mensagemErro: $it")
                            Text(text = it, color = Color.Red)
                        }
                    }
                    TextField(
                        value = state.newPasswordVerification,
                        onValueChange = { state.onNewPasswordVerificationChanged(it) },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text(text = "Confirme Senha Nova")
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        ),
                        maxLines = 1,
                        trailingIcon = {
                            if (state.newPasswordVerification.isNotEmpty()) {
                                Icon(
                                    painter = if (state.isShowNewPasswordVerification) {
                                        painterResource(R.drawable.ic_action_visible)
                                    } else {
                                        painterResource(R.drawable.ic_action_visibility_off)
                                    },
                                    contentDescription = "icone password visible",
                                    modifier = Modifier.clickable {
                                        onChangeNewPasswordVerificationVisibility(state.isShowNewPasswordVerification)
                                    },
                                    tint = Color.DarkGray
                                )
                            }
                        },
                        visualTransformation = if (state.isShowNewPasswordVerification) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        }
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text(text = "Cancel")
                        }
                        TextButton(onClick = { onConfirm() }) {
                            Text(text = "Confirm")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PasswordFormDialogPreview() {
    PasswordFormDialog(state = PasswordFormDialogUiState())
}

@Preview(showBackground = true)
@Composable
private fun PasswordFormDialogWithErrorPreview() {
    PasswordFormDialog(
        state = PasswordFormDialogUiState(
            newPasswordVerificationError = "Erro na confirmação: senhas divergentes",
            passwordErrorMessage = "Erro: Não foi possível redefinir a senha"
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun PasswordFormDialogLoadingPreview() {
    PasswordFormDialog(
        state = PasswordFormDialogUiState(
            isLoading = true
        )
    )
}