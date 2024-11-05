package br.com.steventung.movinlist.ui.dialogs.passwordformdialog

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.steventung.movinlist.repository.FirebaseAuthRepository
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val PASSWORD_FORM_VIEWMODEL_TAG = "PasswordFormDialogViewModel"

@HiltViewModel
class PasswordFormDialogViewModel @Inject constructor(
    private val firebaseAuthRepository: FirebaseAuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(PasswordFormDialogUiState())
    val uiState = _uiState.asStateFlow()
    private val _isChangePasswordSuccess = MutableSharedFlow<Boolean>()
    val isChangePasswordSuccess = _isChangePasswordSuccess.asSharedFlow()

    init {
        _uiState.update { currentState ->
            currentState.copy(
                onCurrentPasswordChanged = { currentPassword ->
                    _uiState.update {
                        it.copy(currentPassword = currentPassword)
                    }
                },
                onNewPasswordChanged = { newPassword ->
                    _uiState.update {
                        it.copy(newPassword = newPassword)
                    }
                },
                onNewPasswordVerificationChanged = { newPasswordVerification ->
                    _uiState.update {
                        it.copy(newPasswordVerification = newPasswordVerification)
                    }
                },
                onCurrentPasswordVisibilityChanged = { showCurrentPassword ->
                    _uiState.update {
                        it.copy(isShowCurrentPassword = showCurrentPassword)
                    }
                },
                onNewPasswordVisibilityChanged = { showNewPassword ->
                    _uiState.update {
                        it.copy(isShowNewPassword = showNewPassword)
                    }
                },
                onNewPasswordVerificationVisibilityChanged = { showNewPasswordVerification ->
                    _uiState.update {
                        it.copy(isShowNewPasswordVerification = showNewPasswordVerification)
                    }
                }
            )
        }
    }

    fun changePassword() {
        viewModelScope.launch {
            try {
                verifyNewPasswordConfirmation()
                showLoadingScreen()
                firebaseAuthRepository.changePassword(
                    currentPassword = _uiState.value.currentPassword,
                    newPassword = _uiState.value.newPassword
                )
                _isChangePasswordSuccess.emit(true)
            } catch (e: Exception) {
                dismissLoadingScreen()
                Log.e(PASSWORD_FORM_VIEWMODEL_TAG, "changePassword:", e)
                updateErrorMessages(e)
            }
        }
    }

    private fun dismissLoadingScreen() {
        _uiState.update {
            it.copy(isLoading = false)
        }
    }

    private fun showLoadingScreen() {
        _uiState.update {
            it.copy(isLoading = true)
        }
    }

    private fun verifyNewPasswordConfirmation() {
        _uiState.update {
            it.copy(newPasswordVerificationError = null)
        }
        if (_uiState.value.newPassword != _uiState.value.newPasswordVerification) {
            _uiState.update {
                it.copy(newPasswordVerificationError = "Erro na confirmação: senhas divergentes")
            }
            throw Exception("Senhas diferentes")
        }
    }

    private suspend fun updateErrorMessages(e: Exception) {
        val errorMessage = defineErrorMessage(e)
        _uiState.update {
            it.copy(passwordErrorMessage = errorMessage)
        }
        delay(4000)
        _uiState.update {
            it.copy(
                passwordErrorMessage = null,
                newPasswordVerificationError = null
            )
        }
    }

    private fun defineErrorMessage(e: Exception) = when (e) {
        is FirebaseAuthWeakPasswordException -> "Erro: Senha deve conter pelo menos 6 caracteres"
        is FirebaseAuthInvalidUserException -> "Erro: Conta não identificada"
        is FirebaseAuthInvalidCredentialsException -> "Erro: Senha atual incorreta"
        is IllegalArgumentException -> "Erro: Preencha todos os campos corretamente"
        is FirebaseNetworkException -> "Erro: Sem conexão de rede"
        else -> "Erro: Não foi possível redefinir a senha"
    }

    fun setCurrentPasswordVisibility(show: Boolean) {
        _uiState.value.onCurrentPasswordVisibilityChanged(!show)
    }

    fun setNewPasswordVisibility(show: Boolean) {
        _uiState.value.onNewPasswordVisibilityChanged(!show)
    }

    fun setNewPasswordVerificationVisibility(show: Boolean) {
        _uiState.value.onNewPasswordVerificationVisibilityChanged(!show)
    }

}
