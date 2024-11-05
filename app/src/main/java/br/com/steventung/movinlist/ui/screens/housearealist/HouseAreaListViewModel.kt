package br.com.steventung.movinlist.ui.screens.housearealist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.steventung.movinlist.repository.FirebaseAuthRepository
import br.com.steventung.movinlist.repository.HouseAreaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HouseAreaListViewModel @Inject constructor(
    private val firebaseAuthRepository: FirebaseAuthRepository,
    private val houseAreaRepository: HouseAreaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HouseAreaListUiState())
    val uiState = _uiState.asStateFlow()

    private val userId = firebaseAuthRepository.currentUserEmail

    init {
        searchHouseAreas()
    }

    private fun searchHouseAreas() {
        viewModelScope.launch {
            userId?.let { userId ->
                houseAreaRepository.getHouseAreasByUserId(userId).collect { houseAreaList ->
                    _uiState.update {
                        it.copy(houseAreas = houseAreaList)
                    }
                }
            }
        }
    }

}