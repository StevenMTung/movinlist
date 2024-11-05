package br.com.steventung.movinlist.ui.screens.budget

import java.math.BigDecimal

data class BudgetUiState(
    val pendingBudget: BigDecimal = BigDecimal.ZERO,
    val purchasedBudget: BigDecimal = BigDecimal.ZERO,
    val totalExpensesBudget: BigDecimal = BigDecimal.ZERO,
    val pendingProductsSection: Map<String, BigDecimal> = emptyMap(),
    val purchasedProductsSection: Map<String, BigDecimal> = emptyMap(),
    val totalExpensesProductsSection: Map<String, BigDecimal> = emptyMap(),
    val pendingIsExpanded: Boolean = false,
    val purchasedIsExpanded: Boolean = false,
    val totalExpensesIsExpanded: Boolean = false,
    val onPendingExpandedStateChanged: (Boolean) -> Unit = {},
    val onPurchasedExpandedStateChanged: (Boolean) -> Unit = {},
    val onTotalExpensesExpandedStateChanged: (Boolean) -> Unit = {}
    )