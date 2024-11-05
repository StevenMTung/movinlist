package br.com.steventung.movinlist.ui.screens.userform

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.steventung.movinlist.domain.model.User
import br.com.steventung.movinlist.extensions.isNetworkAvailable
import br.com.steventung.movinlist.repository.FirebaseAuthRepository
import br.com.steventung.movinlist.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@HiltViewModel
class UserFormViewModel @Inject constructor(
    private val firebaseAuthRepository: FirebaseAuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(UserFormUiState())
    val uiState = _uiState.asStateFlow()
    private val _isUpdateSuccess = MutableSharedFlow<Boolean>()
    val isUpdateSuccess = _isUpdateSuccess.asSharedFlow()
    private val _isSendUserImageFailed = MutableSharedFlow<Boolean>()
    val isSendUserImageFailed = _isSendUserImageFailed.asSharedFlow()
    private val userId = firebaseAuthRepository.currentUserEmail

    init {
        _uiState.update { currentState ->
            currentState.copy(
                onNameChanged = { name ->
                    _uiState.update {
                        it.copy(userName = name)
                    }
                },
                onModalBottomSheetStateChanged = { isShowBottomSheet ->
                    _uiState.update {
                        it.copy(isShowModalBottomSheet = isShowBottomSheet)
                    }
                },
                onImageChanged = { url ->
                    _uiState.update {
                        it.copy(userImage = url)
                    }
                }
            )
        }
        loadUserInfo()
    }

    private fun loadUserInfo() {
        userId?.let { userId ->
            viewModelScope.launch {
                userRepository.getUser(userId).collect { user ->
                    _uiState.update {
                        it.copy(
                            userId = user.userId,
                            userImage = user.picture,
                            userName = user.name
                        )
                    }
                }
            }
        }
    }

    fun updateUser(context: Context) {
        viewModelScope.launch {
            try {
                val user = userId?.let {
                    User(
                        userId = it,
                        name = _uiState.value.userName,
                        picture = _uiState.value.userImage
                    )
                }
                user?.let {
                    _uiState.update {
                        it.copy(isLoading = true)
                    }
                    withTimeout(4000) {
                        if (context.isNetworkAvailable()) {
                            userRepository.updateUser(it)
                        } else {
                            if (!it.picture.startsWith("http")) {
                                userRepository.updateUser(it.copy(picture = ""))
                            } else {
                                userRepository.updateUser(it)
                            }
                        }
                        if (!it.picture.startsWith("http")) {
                            val imageData = setImageDataToByteArray(it, context)
                            _isUpdateSuccess.emit(true)
                            userRepository.sendUserImage(
                                imageData = imageData,
                                userId = user.userId
                            )
                        } else {
                            _isUpdateSuccess.emit(true)
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false)
                }
                when (e) {
                    is TimeoutCancellationException -> {
                        _isSendUserImageFailed.emit(true)
                    }

                    else -> Log.e("UserFormViewModel", "updateUser: ", e)
                }
                Log.e("UserFormViewModel", "updateUser: ", e)
            }
        }
    }

    private fun setImageDataToByteArray(
        it: User,
        context: Context
    ): ByteArray = if (it.picture.isNotEmpty()) {
        val imageUri = Uri.parse(it.picture)
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

    fun setModalBottomSheetState(show: Boolean) {
        _uiState.value.onModalBottomSheetStateChanged(show)
    }

    fun loadImage(url: String) {
        _uiState.value.onImageChanged(url)
    }
}