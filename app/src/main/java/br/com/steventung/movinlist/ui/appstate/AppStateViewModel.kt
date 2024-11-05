package br.com.steventung.movinlist.ui.appstate

import androidx.lifecycle.ViewModel
import br.com.steventung.movinlist.domain.model.User
import br.com.steventung.movinlist.repository.FirebaseAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class AppStateViewModel @Inject constructor(
    private val firebaseAuthRepository: FirebaseAuthRepository
) : ViewModel() {

    private val _appState = MutableStateFlow(AppStateUiState())
    val appState =
        _appState.combine(firebaseAuthRepository.currentUser) { appState, authResult ->
            val user = authResult.currentUser?.email?.let { User(userId = it) }
            appState.copy(
                user = user,
                isInitLoading = authResult.isInitLoading
            )
        }
}