package br.com.steventung.movinlist.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.steventung.movinlist.ui.screens.budget.BudgetScreen
import br.com.steventung.movinlist.ui.screens.budget.BudgetViewModel

internal const val budgetRoute = "budgetRoute"

fun NavGraphBuilder.budgetgraph() {
    composable(route = budgetRoute) {
        val viewModel = hiltViewModel<BudgetViewModel>()
        val state by viewModel.uiState.collectAsState()
        BudgetScreen(
            state = state,
            onExpandPendingBudgetSection = {
                viewModel.setExpandedPendingSection()
            },
            onExpandPurchasedBudgetSection = {
                viewModel.setExpandedPurchasedSection()
            },
            onExpandTotalExpensesBudgetSection = {
                viewModel.setExpandedTotalExpensesSection()
            }
        )
    }
}

fun NavController.navigateToBudget(navOptions: NavOptions? = null) {
    navigate(budgetRoute, navOptions)
}