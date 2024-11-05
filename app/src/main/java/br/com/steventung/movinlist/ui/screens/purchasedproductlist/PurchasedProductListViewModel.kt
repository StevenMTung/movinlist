package br.com.steventung.movinlist.ui.screens.purchasedproductlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.steventung.movinlist.repository.FirebaseAuthRepository
import br.com.steventung.movinlist.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val PUCHASED_PRODUCT_LIST_TAG = "PurchasedProductListViewModel"

@HiltViewModel
class PurchasedProductListViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    firebaseAuthRepository: FirebaseAuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PurchasedProductListUiState())
    val uiState = _uiState.asStateFlow()

    private val userId = firebaseAuthRepository.currentUserEmail

    init {
        _uiState.update { currentState ->
            currentState.copy(
                onSearchedTextChanged = { searchedText ->
                    _uiState.update {
                        it.copy(searchedText = searchedText)
                    }
                    loadPurchasedProducts()
                }
            )
        }
        loadPurchasedProducts()
    }

    fun loadPurchasedProducts() {
        userId?.let {
            viewModelScope.launch {
                try {
                    if (_uiState.value.searchedText.isBlank()) {
                        loadAllPurchasedProducts(it)
                    } else {
                        loadSearchedPurchasedProducts(it)
                    }
                } catch (e: Exception) {
                    Log.e(PUCHASED_PRODUCT_LIST_TAG, "loadPurchasedProducts: ", e)
                    _uiState.update {
                        it.copy(sections = emptyMap())
                    }
                }
            }
        }
    }

    private suspend fun loadSearchedPurchasedProducts(it: String) {
        val productList =
            productRepository.getPurchasedProductByUserIdAndProductName(
                userId = it,
                searchedText = _uiState.value.searchedText
            ).first()
        val productSection = productList.groupBy { it.houseAreaName }
        _uiState.update {
            it.copy(sections = productSection)
        }
    }

    private suspend fun loadAllPurchasedProducts(it: String) {
        productRepository.getPurchasedProductByUserId(userId = it)
            .collect { purchasedProductList ->
                val purchasedProductSection = purchasedProductList.groupBy {
                    it.houseAreaName
                }
                _uiState.update {
                    it.copy(sections = purchasedProductSection)
                }
            }
    }
}