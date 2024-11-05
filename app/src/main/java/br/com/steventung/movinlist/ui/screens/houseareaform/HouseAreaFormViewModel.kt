package br.com.steventung.movinlist.ui.screens.houseareaform

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.steventung.movinlist.domain.model.HouseArea
import br.com.steventung.movinlist.navigation.houseAreaIdArgument
import br.com.steventung.movinlist.navigation.houseAreaNameArgument
import br.com.steventung.movinlist.repository.HouseAreaRepository
import br.com.steventung.movinlist.repository.FirebaseAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

private const val TAG = "HouseAreaFormViewModel"

@HiltViewModel
class HouseAreaFormViewModel @Inject constructor(
    private val houseAreaRepository: HouseAreaRepository,
    private val firebaseAuthRepository: FirebaseAuthRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(HouseAreaFormUiState())
    val uiState = _uiState.asStateFlow()
    private val _isSaveSuccess = MutableSharedFlow<String>()
    val isSaveSuccess = _isSaveSuccess.asSharedFlow()

    private val houseAreaName = savedStateHandle.get<String>(houseAreaNameArgument)
    private val houseAreaId = savedStateHandle.get<String>(houseAreaIdArgument)

    init {
        _uiState.update { currentState ->
            currentState.copy(
                onHouseAreaNameChanged = { name ->
                    _uiState.update { it.copy(houseAreaName = name) }
                }
            )
        }
        loadHouseAreaName()
    }

    private fun loadHouseAreaName() {
        houseAreaName?.let { houseAreaName ->
            _uiState.update {
                it.copy(houseAreaName = houseAreaName)
            }
        }
    }

    fun saveHouseArea(context: Context) {
        viewModelScope.launch {
            try {
                if (_uiState.value.houseAreaName.isBlank()) {
                    showEmptyHouseAreaNameErrorMessage()
                } else {
                    firebaseAuthRepository.currentUserEmail?.let { currentUserEmail ->
                        val houseArea = HouseArea(
                            houseAreaId = houseAreaId,
                            houseAreaName = _uiState.value.houseAreaName,
                            userId = currentUserEmail
                        )
                        showLoadingScreen()
                        houseArea.houseAreaId?.let {
                            if (isNetworkAvailable(context)) {
                                houseAreaRepository.saveHouseArea(houseArea = houseArea)
                            } else {
                                throw RuntimeException()
                            }
                        } ?: withTimeout(3000) {
                            houseAreaRepository.saveHouseArea(houseArea = houseArea)
                        }
                        _isSaveSuccess.emit("$currentUserEmail-${_uiState.value.houseAreaName}")
                    } ?: throw Exception()
                }
            } catch (e: Exception) {
                dismissLoadingScreen()
                when (e) {
                    is IllegalArgumentException -> {
                        showConflictErrorMessage()
                    }

                    is TimeoutCancellationException -> {
                        firebaseAuthRepository.currentUserEmail?.let { currentUserEmail ->
                            _isSaveSuccess.emit("$currentUserEmail-${_uiState.value.houseAreaName}")
                        }
                    }

                    is RuntimeException -> {
                        showEditHouseAreaErrorMessage()
                    }

                    else -> {
                        Log.e(TAG, "save: Exception", e)
                        showStandardErrorMessage()
                    }
                }
            }
        }
    }

    private suspend fun showStandardErrorMessage() {
        _uiState.update {
            it.copy(networkErrorMessage = "Erro: Não foi possível criar/editar cômodo, verifique a conexão de rede")
        }
        delay(4000)
        _uiState.update {
            it.copy(networkErrorMessage = null)
        }
    }

    private suspend fun showEditHouseAreaErrorMessage() {
        _uiState.update {
            it.copy(networkErrorMessage = "Erro: Não foi possível editar o cômodo, verifique a conexão de rede")
        }
        delay(4000)
        _uiState.update {
            it.copy(networkErrorMessage = null)
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

    private suspend fun showConflictErrorMessage() {
        _uiState.update {
            it.copy(isShowConflictError = true)
        }
        delay(4000)
        _uiState.update {
            it.copy(isShowConflictError = false)
        }
    }

    private suspend fun showEmptyHouseAreaNameErrorMessage() {
        _uiState.update {
            it.copy(isShowErrorMessage = true)
        }
        delay(4000)
        _uiState.update {
            it.copy(isShowErrorMessage = false)
        }
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
    }
}