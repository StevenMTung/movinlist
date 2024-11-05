package br.com.steventung.movinlist.ui.screens.userform

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.steventung.mobilist.R
import coil.compose.AsyncImage

@Composable
fun UserFormScreen(
    modifier: Modifier = Modifier,
    state: UserFormUiState,
    onChangeUserPhoto: () -> Unit = {},
    onSaveUserUpdates: () -> Unit = {},
    onOpenChangePasswordDialog: () -> Unit = {},
    onDeleteUser: () -> Unit = {}
) {
    if (state.isLoading) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            Text(
                text = "Carregando...",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .windowInsetsPadding(WindowInsets.ime)
        ) {
            Column(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                AsyncImage(
                    model = state.userImage,
                    contentDescription = "profile photo",
                    modifier = Modifier
                        .size(250.dp)
                        .clip(shape = CircleShape)
                        .align(Alignment.CenterHorizontally),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.default_profile_picture),
                    error = painterResource(R.drawable.default_profile_picture)
                )
                TextButton(
                    onClick = onChangeUserPhoto,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(text = "Trocar foto")
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                TextField(
                    value = state.userId,
                    onValueChange = {},
                    enabled = false,
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Email, contentDescription = "ícone email")
                    },
                    label = {
                        Text(text = "E-mail")
                    }
                )
                TextField(
                    value = state.userName,
                    onValueChange = { state.onNameChanged(it) },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "ícone usuário"
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words
                    ),
                    maxLines = 1,
                    label = {
                        Text(text = "Nome usuário")
                    }
                )
                TextField(
                    value = state.userName,
                    onValueChange = { state.onNameChanged },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false,
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Lock, contentDescription = "ícone senha")
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    label = {
                        Text(text = "Senha")
                    }
                )
            }
            TextButton(
                onClick = onOpenChangePasswordDialog,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Alterar senha")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = onSaveUserUpdates,
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),

                    ) {
                    Text(text = "Salvar alterações")
                }
                Button(
                    onClick = onDeleteUser,
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text(text = "Excluir conta")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UserFormScreenPreview() {
    UserFormScreen(
        state = UserFormUiState(
            userId = "teste@mobilist.com",
            userName = "Jose Bonifácio"
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun UserFormScreenLoadingPreview() {
    UserFormScreen(
        state = UserFormUiState(
            isLoading = true
        )
    )
}