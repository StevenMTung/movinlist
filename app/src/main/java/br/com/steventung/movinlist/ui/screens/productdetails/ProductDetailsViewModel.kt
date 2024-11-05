package br.com.steventung.movinlist.ui.screens.productdetails

import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.steventung.movinlist.extensions.isNetworkAvailable
import br.com.steventung.movinlist.navigation.productIdArgument
import br.com.steventung.movinlist.repository.ProductRepository
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

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val productRepository: ProductRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductDetailsUiState())
    val uiState = _uiState.asStateFlow()
    private val _isClickButtonSucess = MutableSharedFlow<Boolean>()
    val isClickButtonSuccess = _isClickButtonSucess.asSharedFlow()

    private val productId = savedStateHandle.get<String>(productIdArgument)

    init {
        _uiState.update { currentState ->
            currentState.copy(
                onDeleteAlertDialogStateChanged = { isShowDeleteAlertDialog ->
                    _uiState.update {
                        it.copy(isShowDeleteAlertDialog = isShowDeleteAlertDialog)
                    }
                }
            )
        }
        loadProductDetails()
    }

    private fun loadProductDetails() {
        productId?.let { id ->
            viewModelScope.launch {
                productRepository.getProductById(id).collect { product ->
                    _uiState.update {
                        it.copy(
                            image = product.image,
                            name = product.name,
                            description = product.description,
                            brand = product.brand,
                            price = product.price,
                            quantity = product.quantity.toString(),
                            houseAreaName = product.houseAreaName,
                            purchased = product.purchased,
                        )
                    }
                }
            }
        }
    }

    fun addProductToPurchasedList() {
        productId?.let { productId ->
            viewModelScope.launch {
                try {
                    showLoadingScreen()
                    withTimeout(3000) {
                        productRepository.upDateProductPurchaseState(
                            purchasedState = true,
                            productId = productId
                        )
                    }
                    _isClickButtonSucess.emit(true)
                } catch (e: Exception) {
                    dismissLoadingScreen()
                    when (e) {
                        is TimeoutCancellationException -> {
                            _isClickButtonSucess.emit(true)
                        }

                        else -> Log.e(
                            "ProductDetailsViewModel",
                            "addProductToPurchasedList: Exception",
                            e
                        )
                    }
                }
            }
        }
    }

    fun returnProductToList() {
        productId?.let { productId ->
            viewModelScope.launch {
                try {
                    showLoadingScreen()
                    withTimeout(3000) {
                        productRepository.upDateProductPurchaseState(
                            purchasedState = false,
                            productId = productId
                        )
                    }
                    _isClickButtonSucess.emit(true)
                } catch (e: Exception) {
                    dismissLoadingScreen()
                    when (e) {
                        is TimeoutCancellationException -> {
                            _isClickButtonSucess.emit(true)
                        }

                        else -> Log.e(
                            "ProductDetailsViewModel",
                            "addProductToPurchasedList: Exception",
                            e
                        )
                    }
                }
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

    fun removeProduct(context: Context) {
        productId?.let { productId ->
            viewModelScope.launch {
                try {
                    if(context.isNetworkAvailable()){
                        productRepository.removeProductById(productId = productId)
                    } else {
                        throw Exception()
                    }
                    _isClickButtonSucess.emit(true)
                    productRepository.removeProductImage(productId = productId)
                } catch (e: Exception) {
                    updateDeleteErrorMessage()
                    Log.e("ProductDetailsViewModel", "removeProduct: Exception", e)
                }
            }
        }
    }

    private suspend fun updateDeleteErrorMessage() {
        _uiState.update {
            it.copy(deleteErrorMessage = "Falha ao remover produto: Verifique sua conexão de rede")
        }
        delay(4000)
        _uiState.update {
            it.copy(deleteErrorMessage = null)
        }
    }

    fun setDeleteAlertDialog(show: Boolean) {
        _uiState.value.onDeleteAlertDialogStateChanged(show)
    }

}