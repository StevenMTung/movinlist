package br.com.steventung.movinlist.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.steventung.movinlist.ui.screens.purchasedproductlist.PurchasedProductListScreen
import br.com.steventung.movinlist.ui.screens.purchasedproductlist.PurchasedProductListViewModel

internal const val purchasedProductsRoute = "purchasedProductsRoute"

fun NavGraphBuilder.purchasedProductListGraph(
    onNavigateToProductDetails: (String) -> Unit
) {
    composable(route = purchasedProductsRoute) {
        val viewModel = hiltViewModel<PurchasedProductListViewModel>()
        val state by viewModel.uiState.collectAsState()
        PurchasedProductListScreen(
            state = state,
            onPurchasedProductItemClick = { productId ->
                onNavigateToProductDetails(productId)
            },
            onRefreshScreen = {
                viewModel.loadPurchasedProducts()
            }
        )
    }
}

fun NavController.navigateToPurchasedProducts(navOptions: NavOptions? = null) {
    navigate(purchasedProductsRoute, navOptions)
}