package br.com.steventung.movinlist.ui.screens.login

import android.util.Log
import androidx.lifecycle.ViewModel
import br.com.steventung.movinlist.repository.FirebaseAuthRepository
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

const val TAG = "LoginViewModel"

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseAuthRepository: FirebaseAuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> get() = _uiState.asStateFlow()

    init {
        _uiState.update { currentState ->
            currentState.copy(
                onEmailChanged = { email ->
                    _uiState.update { it.copy(email = email) }
                },
                onPasswordChanged = { password ->
                    _uiState.update { it.copy(password = password) }
                }
            )
        }
    }

    suspend fun signIn() {
        try {
            firebaseAuthRepository.signIn(
                email = _uiState.value.email,
                senha = _uiState.value.password
            )
        } catch (e: Exception) {
            Log.e(TAG, "signIn Exception: ", e)
            upDateSignInErrorMessage(e)
        }
    }

    private suspend fun upDateSignInErrorMessage(e: Exception) {
        val errorMessage = defineErrorMessage(e)
        _uiState.update {
            it.copy(error = errorMessage)
        }
        delay(3000)
        _uiState.update {
            it.copy(error = null)
        }
    }

    private fun defineErrorMessage(e: Exception) = when (e) {
        is FirebaseAuthInvalidUserException -> "E-mail não cadastrado"
        is FirebaseAuthInvalidCredentialsException -> "E-mail ou senha incorreto"
        is FirebaseNetworkException -> "Erro na autenticação do usuário: Verifique conexão de rede"
        else -> "Erro na autenticação do usuário"
    }

    fun changePasswordVisibility(visibility: Boolean) {
        _uiState.update {
            it.copy(passwordVisibility = !visibility)
        }
    }
}