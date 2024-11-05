package br.com.steventung.movinlist.ui.screens.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.steventung.movinlist.repository.ProductRepository
import br.com.steventung.movinlist.repository.FirebaseAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class BudgetViewModel @Inject constructor(
    firebaseAuthRepository: FirebaseAuthRepository,
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BudgetUiState())
    val uiState = _uiState.asStateFlow()

    private val userId = firebaseAuthRepository.currentUserEmail

    init {
        _uiState.update { currentState ->
            currentState.copy(
                onPendingExpandedStateChanged = { expandedState ->
                    _uiState.update {
                        it.copy(pendingIsExpanded = !expandedState)
                    }
                },
                onPurchasedExpandedStateChanged = { expandedState ->
                    _uiState.update {
                        it.copy(purchasedIsExpanded = !expandedState)
                    }
                },
                onTotalExpensesExpandedStateChanged = { expandedState ->
                    _uiState.update {
                        it.copy(totalExpensesIsExpanded = !expandedState)
                    }
                }
            )
        }
        loadPendingProductsBudget()
        loadPurchasedProductsBudget()
    }

    private fun loadPendingProductsBudget() {
        userId?.let { userId ->
            viewModelScope.launch {
                productRepository.getNonPurchasedProductByUserId(userId = userId)
                    .collect { pendingProductList ->
                        val pendingProductMap = pendingProductList
                            .groupBy { it.houseAreaName }
                        val pendingProductSection =
                            pendingProductMap.mapValues { section ->
                                section.value.fold(BigDecimal.ZERO) { acc, product ->
                                    acc + (product.price * BigDecimal(product.quantity))
                                }
                            }
                        val pendingBudgetValue = pendingProductSection.values.sumOf { it }
                        _uiState.update {
                            it.copy(
                                pendingProductsSection = pendingProductSection,
                                pendingBudget = pendingBudgetValue,
                                totalExpensesBudget = _uiState.value.purchasedBudget + pendingBudgetValue,
                                totalExpensesProductsSection = (_uiState.value.purchasedProductsSection.keys + pendingProductSection.keys)
                                    .associateWith { key ->
                                        (_uiState.value.purchasedProductsSection[key]
                                            ?: BigDecimal.ZERO) +
                                                (pendingProductSection[key] ?: BigDecimal.ZERO)
                                    }
                            )
                        }
                    }
            }
        }
    }

    private fun loadPurchasedProductsBudget() {
        userId?.let { userId ->
            viewModelScope.launch {
                productRepository.getPurchasedProductByUserId(userId = userId)
                    .collect { purchasedProductList ->
                        val purchasedProductMap = purchasedProductList
                            .groupBy { it.houseAreaName }
                        val purchasedProductSection =
                            purchasedProductMap.mapValues { section ->
                                section.value.fold(BigDecimal.ZERO) { acc, product ->
                                    acc + (product.price * BigDecimal(product.quantity))
                                }
                            }
                        val purchasedBudgetValue = purchasedProductSection.values.sumOf { it }
                        _uiState.update {
                            it.copy(
                                purchasedProductsSection = purchasedProductSection,
                                purchasedBudget = purchasedBudgetValue,
                                totalExpensesBudget = _uiState.value.pendingBudget + purchasedBudgetValue,
                                totalExpensesProductsSection = (_uiState.value.pendingProductsSection.keys + purchasedProductSection.keys)
                                    .associateWith { key ->
                                        (_uiState.value.pendingProductsSection[key]
                                            ?: BigDecimal.ZERO) +
                                                (purchasedProductSection[key] ?: BigDecimal.ZERO)
                                    }
                            )
                        }
                    }
            }
        }
    }

    fun setExpandedPendingSection() {
        _uiState.value.onPendingExpandedStateChanged(_uiState.value.pendingIsExpanded)
    }

    fun setExpandedPurchasedSection() {
        _uiState.value.onPurchasedExpandedStateChanged(_uiState.value.purchasedIsExpanded)
    }

    fun setExpandedTotalExpensesSection() {
        _uiState.value.onTotalExpensesExpandedStateChanged(_uiState.value.totalExpensesIsExpanded)
    }
}