package br.com.steventung.movinlist.ui.screens.signup

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.steventung.movinlist.domain.model.User
import br.com.steventung.movinlist.repository.FirebaseAuthRepository
import br.com.steventung.movinlist.repository.UserRepository
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject

private const val SIGN_UP_TAG = "SignUpViewModel"

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val firebaseAuthRepository: FirebaseAuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> get() = _uiState.asStateFlow()
    private val _isSignUpSucess = MutableSharedFlow<Boolean>()
    val isSignUpSuccess = _isSignUpSucess.asSharedFlow()

    init {
        _uiState.update { currentState ->
            currentState.copy(
                onNameChanged = { name ->
                    _uiState.update {
                        it.copy(name = name)
                    }
                },
                onEmailChanged = { email ->
                    _uiState.update {
                        it.copy(email = email)
                    }
                },
                onPasswordChanged = { password ->
                    _uiState.update {
                        it.copy(password = password)
                    }
                },
                onPasswordVerifyChanged = { passwordVerify ->
                    _uiState.update {
                        it.copy(passwordVerify = passwordVerify)
                    }
                },
                onProfilePhotoChanged = { profilePhoto ->
                    _uiState.update {
                        it.copy(profilePhoto = profilePhoto)
                    }
                },
                onModalBottomSheetStateChanged = { isShowModalBottomSheet ->
                    _uiState.update {
                        it.copy(isShowModalBottomSheet = isShowModalBottomSheet)
                    }
                },
                onCameraOpenStateChanged = { isCameraOpen ->
                    _uiState.update {
                        it.copy(isCameraOpen = isCameraOpen)
                    }
                },
                onPasswordVisibilityChanged = { showPassword ->
                    _uiState.update {
                        it.copy(passwordVisibility = !showPassword)
                    }
                },
                onPasswordVerifyVisibilityChanged = { showPasswordVerify ->
                    _uiState.update {
                        it.copy(passwordVerifyVisibility = !showPasswordVerify)
                    }
                }
            )
        }
    }

    fun signUpUser(context: Context) {
        viewModelScope.launch {
            try {
                verifyPasswordConfirmation()
                firebaseAuthRepository.signUpUser(
                    email = _uiState.value.email,
                    senha = _uiState.value.password
                )
                val user = User(
                    userId = _uiState.value.email,
                    name = _uiState.value.name,
                    picture = _uiState.value.profilePhoto
                )
                userRepository.saveUser(user = user)
                val imageData = if (user.picture.isNotEmpty()) {
                    val imageUri = Uri.parse(user.picture)
                    val inputStream = context.contentResolver.openInputStream(imageUri)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    val baos = ByteArrayOutputStream()
                    bitmap?.let {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    }
                    baos.toByteArray()
                } else {
                    ByteArrayOutputStream().toByteArray()
                }

                _isSignUpSucess.emit(true)

                userRepository.sendUserImage(imageData = imageData, userId = user.userId)

            } catch (e: Exception) {
                Log.e(SIGN_UP_TAG, "signUpUser exception", e)
                upDateSignUpErrorMessage(e)
            }
        }
    }

    private suspend fun upDateSignUpErrorMessage(e: Exception) {
        val errorMessage: String = defineErrorMessage(e)
        _uiState.update {
            it.copy(error = errorMessage)
        }
        delay(4000)
        _uiState.update {
            it.copy(
                error = null,
                passwordVerifyErrorMessage = null
            )
        }
    }

    private fun verifyPasswordConfirmation() {
        _uiState.update {
            it.copy(passwordVerifyErrorMessage = null)
        }
        if (_uiState.value.password != _uiState.value.passwordVerify) {
            _uiState.update {
                it.copy(passwordVerifyErrorMessage = "Erro na confirmação: senhas divergentes")
            }
            throw Exception("Senhas diferentes")
        }
    }

    private fun defineErrorMessage(e: Exception) = when (e) {
        is IllegalArgumentException -> "Preencha todos os campos"

        is FirebaseAuthUserCollisionException -> "E-mail já cadastrado"

        is FirebaseAuthWeakPasswordException -> "Senha deve conter pelo menos 6 caracteres"

        is FirebaseAuthInvalidCredentialsException -> "E-mail inválido"

        is FirebaseNetworkException -> "Erro no cadastro de usuário: Verifique a conexão de rede"

        else -> "Erro no cadastro de usuário"
    }

    fun setModalBottomSheetState(show: Boolean) {
        _uiState.value.onModalBottomSheetStateChanged(show)
    }

    fun loadImage(url: String) {
        _uiState.value.onProfilePhotoChanged(url)
    }

    fun setCameraState(show: Boolean) {
        _uiState.value.onCameraOpenStateChanged(show)
    }

    fun setPasswordVisibility(show: Boolean) {
        _uiState.value.onPasswordVisibilityChanged(show)
    }

    fun setPasswordVerifyVisibility(show: Boolean) {
        _uiState.value.onPasswordVerifyVisibilityChanged(show)
    }
}