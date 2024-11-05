package br.com.steventung.movinlist.ui.screens.houseareaproductlist

import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.steventung.movinlist.extensions.isNetworkAvailable
import br.com.steventung.movinlist.navigation.houseAreaProductListArgument
import br.com.steventung.movinlist.repository.HouseAreaRepository
import br.com.steventung.movinlist.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HouseAreaProductListViewModel @Inject constructor(
    private val houseAreaRepository: HouseAreaRepository,
    private val productRepository: ProductRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(HouseAreaProductListUiState())
    val uiState = _uiState.asStateFlow()
    private val _isDeleteSuccess = MutableSharedFlow<Boolean>()
    val isDeleteSuccess = _isDeleteSuccess.asSharedFlow()

    private val houseAreaId = savedStateHandle.get<String>(houseAreaProductListArgument)


    init {
        _uiState.update { currentState ->
            currentState.copy(
                onDeleteAlertDialogStateChanged = { isShowAlertDialog ->
                    _uiState.update {
                        it.copy(isShowDeleteAlertDialog = isShowAlertDialog)
                    }
                }
            )
        }
        loadHouseAreaNameTitle()
        loadHouseAreaNonPurchasedProducts()
    }

    private fun loadHouseAreaNonPurchasedProducts() {
        houseAreaId?.let { houseAreaId ->
            viewModelScope.launch {
                productRepository.getNonPurchasedProductByHouseAreaId(houseAreaId)
                    .collect { productList ->
                        _uiState.update {
                            it.copy(
                                productList = productList
                            )
                        }
                    }
            }
        }
    }

    private fun loadHouseAreaNameTitle() {
        houseAreaId?.let {
            viewModelScope.launch {
                houseAreaRepository.getHouseAreaById(it).collect { houseArea ->
                    _uiState.update {
                        it.copy(houseAreaName = houseArea.houseAreaName)
                    }
                }
            }
        }
    }

    fun setDeleteAlertDialog(show: Boolean) {
        _uiState.value.onDeleteAlertDialogStateChanged(show)
    }

    fun removeHouseArea(context: Context) {
        viewModelScope.launch {
            houseAreaId?.let { houseAreaId ->
                try {
                    if (context.isNetworkAvailable()) {
                        houseAreaRepository.removeHouseArea(houseAreaId = houseAreaId)
                        _isDeleteSuccess.emit(true)
                        houseAreaRepository.removeProductsWhenHouseAreaIsDeleted(houseAreaId = houseAreaId)
                    } else {
                        showDeleteErrorMessage()
                    }

                } catch (e: Exception) {
                    Log.e("HouseAreaProductListViewModel", "removeHouseArea: Exception", e)
                }
            }
        }
    }

    private suspend fun showDeleteErrorMessage() {
        _uiState.update {
            it.copy(deleteErrorMessage = "Erro: Falha ao remover cômodo, verifique a conexão de rede")
        }
        delay(4000)
        _uiState.update {
            it.copy(deleteErrorMessage = null)
        }
    }
}