package br.com.steventung.movinlist.ui.dialogs.deleteaccountdialog

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.steventung.movinlist.repository.FirebaseAuthRepository
import br.com.steventung.movinlist.repository.UserRepository
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeleteAccountDialogViewModel @Inject constructor(
    private val firebaseAuthRepository: FirebaseAuthRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(DeleteAccountDialogUiState())
    val uiState = _uiState.asStateFlow()

    private val userId = firebaseAuthRepository.currentUserEmail

    init {
        _uiState.update { currentState ->
            currentState.copy(
                onPasswordChanged = { password ->
                    _uiState.update {
                        it.copy(password = password)
                    }
                },
                onChangePasswordVisibilityChanged = { passwordVisibility ->
                    _uiState.update {
                        it.copy(passwordVisibility = passwordVisibility)
                    }
                }
            )
        }
        loadUserEmailField()
    }

    private fun loadUserEmailField() {
        userId?.let { userEmail ->
            _uiState.update {
                it.copy(email = userEmail)
            }
        }
    }

    fun deleteUserAccount() {
        userId?.let {
            viewModelScope.launch {
                try {
                    _uiState.update {
                        it.copy(isDeleteLoading = true)
                    }
                    firebaseAuthRepository.deleteUserAccount(_uiState.value.password)
                    userRepository.deleteUser(it)
                    userRepository.removeImageWhenUserIsDeleted(it)
                    userRepository.deleteUserProducts(it)
                    userRepository.deleteUserHouseAreas(it)
                } catch (e: Exception) {
                    Log.e("DeleteAccountDialogViewModel", "deleteUserAccount: ", e)
                    val errorMessage = defineErrorMessage(e)
                    _uiState.update {
                        it.copy(
                            isDeleteLoading = false,
                            errorMessage = errorMessage
                        )
                    }
                    delay(4000)
                    _uiState.update {
                        it.copy(
                            errorMessage = null
                        )
                    }
                }
            }
        }
    }

    private fun defineErrorMessage(e: Exception) = when (e) {
        is FirebaseAuthInvalidUserException -> "Erro: Usuário não encontrado"
        is FirebaseAuthInvalidCredentialsException -> "Erro: Senha incorreta"
        is IllegalArgumentException -> "Erro: Preencha o campo de senha"
        is FirebaseNetworkException -> "Erro: Sem conexão de rede"

        else -> "Erro: Falha ao remover conta"
    }

    fun setPasswordVisibility(show: Boolean) {
        _uiState.value.onChangePasswordVisibilityChanged(!show)
    }
}