package br.com.steventung.movinlist.ui.dialogs.profiledetailsdialog

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.steventung.movinlist.repository.UserRepository
import br.com.steventung.movinlist.repository.FirebaseAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileDetailsDialogViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val firebaseAuthRepository: FirebaseAuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileDetailsDialogUiState())
    val uiState = _uiState.asStateFlow()

    private val userId = firebaseAuthRepository.currentUserEmail

    init {
        loadUserInfo()
    }

    private fun loadUserInfo() {
        userId?.let {
            viewModelScope.launch {
                userRepository.getUser(it).collect { user ->
                    _uiState.update {
                        Log.i("ProfileDetailsDialogViewModel", "Rodou")
                        it.copy(
                            userImage = user.picture,
                            userName = user.name
                        )
                    }
                }
            }
        }
    }

    fun signOut() {
        _uiState.update {
            it.copy(isLoading = true)
        }
        firebaseAuthRepository.signOut()
    }
}