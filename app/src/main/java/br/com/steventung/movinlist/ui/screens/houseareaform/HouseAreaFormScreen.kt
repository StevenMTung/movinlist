package br.com.steventung.movinlist.ui.screens.houseareaform

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HouseAreaFormScreen(
    modifier: Modifier = Modifier,
    state: HouseAreaFormUiState,
    onCreateHouseArea: () -> Unit = {}
) {
    Column {
        AnimatedVisibility(!state.networkErrorMessage.isNullOrBlank()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.Red)
            ) {
                state.networkErrorMessage?.let {
                    Text(
                        text = it,
                        modifier = Modifier.padding(8.dp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        Column(
            modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(modifier = Modifier.align(Alignment.Center)) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                        Text(
                            text = "Atualizando...",
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
            } else {
                AnimatedVisibility(state.isShowErrorMessage) {
                    Text(
                        text = "Erro: Preencha o nome do cômodo",
                        fontWeight = FontWeight.Bold,
                        color = Color.Red,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
                AnimatedVisibility(state.isShowConflictError) {
                    Text(
                        text = "Erro: Esse cômodo já foi cadastrado",
                        fontWeight = FontWeight.Bold,
                        color = Color.Red,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
                OutlinedTextField(
                    value = state.houseAreaName,
                    onValueChange = { state.onHouseAreaNameChanged(it) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Nome do cômodo") },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words
                    )
                )
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = { onCreateHouseArea() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Salvar")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HouseAreaFormScreenPreview() {
    HouseAreaFormScreen(
        state = HouseAreaFormUiState(
            networkErrorMessage = "Erro: Não foi possível editar o cômodo, verifique a conexão de rede"
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun HouseAreaFormScreenWithEmptyErrorPreview() {
    HouseAreaFormScreen(state = HouseAreaFormUiState(isShowErrorMessage = true))
}

@Preview(showBackground = true)
@Composable
private fun HouseAreaFormScreenWithConflictErrorPreview() {
    HouseAreaFormScreen(state = HouseAreaFormUiState(isShowConflictError = true))
}

@Preview(showBackground = true)
@Composable
private fun HouseAreaFormScreenLoadingPreview() {
    HouseAreaFormScreen(state = HouseAreaFormUiState(isLoading = true))
}