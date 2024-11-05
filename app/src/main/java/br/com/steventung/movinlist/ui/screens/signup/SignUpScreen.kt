package br.com.steventung.movinlist.ui.screens.signup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.steventung.mobilist.R
import coil.compose.AsyncImage

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    state: SignUpUiState,
    onSignUpUser: () -> Unit = {},
    onAddUserPhoto: () -> Unit = {},
    onChangePasswordVisibility: (Boolean) -> Unit = {},
    onChangePasswordVerifyVisibility: (Boolean) -> Unit = {}
) {
    Column(modifier = modifier.fillMaxSize()) {
        AnimatedVisibility(visible = state.error != null) {
            state.error?.let {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.Red)
                ) {
                    Text(
                        text = it,
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .windowInsetsPadding(WindowInsets.ime)
        ) {
            Spacer(
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth()
            )
            Column(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                AsyncImage(
                    model = state.profilePhoto,
                    contentDescription = null,
                    modifier = Modifier
                        .size(200.dp)
                        .clip(shape = CircleShape),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.default_profile_picture),
                    error = painterResource(R.drawable.default_profile_picture)
                )
                TextButton(
                    onClick = onAddUserPhoto,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(text = "Adicionar foto")
                }
            }
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = state.name,
                    onValueChange = { state.onNameChanged(it) },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "icone nome usuario"
                        )
                    },
                    label = { Text(text = "Nome") },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next
                    )
                )
                OutlinedTextField(
                    value = state.email,
                    onValueChange = { state.onEmailChanged(it) },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(
                            Icons.Default.Email,
                            contentDescription = "icone e-mail"
                        )
                    },
                    label = { Text(text = "E-mail") },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    )
                )
                OutlinedTextField(
                    value = state.password,
                    onValueChange = { state.onPasswordChanged(it) },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = "icone senha"
                        )
                    },
                    label = { Text(text = "Senha") },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
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
                    visualTransformation = if (state.passwordVisibility) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    }
                )
                AnimatedVisibility(state.passwordVerifyErrorMessage != null) {
                    state.passwordVerifyErrorMessage?.let {
                        Text(text = it, color = Color.Red)
                    }
                }

                OutlinedTextField(
                    value = state.passwordVerify,
                    onValueChange = { state.onPasswordVerifyChanged(it) },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = "icone senha"
                        )
                    },
                    label = { Text(text = "Confirmar senha") },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    trailingIcon = {
                        if (state.passwordVerify.isNotEmpty()) {
                            Icon(
                                painter = if (state.passwordVerifyVisibility) {
                                    painterResource(R.drawable.ic_action_visible)
                                } else {
                                    painterResource(R.drawable.ic_action_visibility_off)
                                },
                                contentDescription = "icone password visible",
                                modifier = Modifier.clickable {
                                    onChangePasswordVerifyVisibility(state.passwordVerifyVisibility)
                                },
                                tint = Color.DarkGray
                            )
                        }
                    },
                    visualTransformation = if (state.passwordVerifyVisibility) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    }
                )
            }

            Button(
                onClick = { onSignUpUser() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = "Cadastrar usuário")
            }
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SignUpScreenPreview() {
    SignUpScreen(state = SignUpUiState())
}

@Preview(showBackground = true)
@Composable
private fun SignUpScreenWithErrorPreview() {
    SignUpScreen(
        state = SignUpUiState(
            error = "Erro",
            passwordVerifyErrorMessage = "Erro na confirmação: senhas divergentes"
        )
    )
}