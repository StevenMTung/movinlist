package br.com.steventung.movinlist.ui.screens.allproductlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.steventung.movinlist.repository.ProductRepository
import br.com.steventung.movinlist.repository.FirebaseAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val ALL_PRODUCT_LIST_TAG = "AllProductListViewModel"

@HiltViewModel
class AllProductListViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    firebaseAuthRepository: FirebaseAuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AllProductListUiState())
    val uiState = _uiState.asStateFlow()

    private val userId = firebaseAuthRepository.currentUserEmail

    init {
        _uiState.update { currentState ->
            currentState.copy(
                onSearchedText = { searchedText ->
                    _uiState.update {
                        it.copy(searchedText = searchedText)
                    }
                    loadProductSection()
                }
            )
        }
        loadProductSection()
    }

    fun loadProductSection() {
        viewModelScope.launch {
            try {
                userId?.let { userId ->
                    if (_uiState.value.searchedText.isBlank()) {
                        loadAllNonPurchasedProducts(userId)
                    } else {
                        loadSearchedNonPurchasedProducts(userId)
                    }
                }
            } catch (e: Exception) {
                Log.e(ALL_PRODUCT_LIST_TAG, "loadProductSection: ", e)
                _uiState.update {
                    it.copy(sections = emptyMap())
                }
            }
        }
    }

    private suspend fun loadSearchedNonPurchasedProducts(userId: String) {
        val productList =
            productRepository.getNonPurchasedProductByUserIdAndProductName(
                userId = userId,
                searchedText = _uiState.value.searchedText
            ).first()
        val productSection = productList.groupBy { it.houseAreaName }
        _uiState.update {
            it.copy(sections = productSection)
        }
    }

    private suspend fun loadAllNonPurchasedProducts(userId: String) {
        productRepository.getNonPurchasedProductByUserId(userId)
            .collect { productList ->
                val productSection = productList.groupBy { it.houseAreaName }
                _uiState.update {
                    it.copy(sections = productSection)
                }
            }
    }
}