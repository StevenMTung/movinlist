package br.com.steventung.movinlist.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.steventung.movinlist.ui.screens.allproductlist.AllProductListScreen
import br.com.steventung.movinlist.ui.screens.allproductlist.AllProductListViewModel

internal const val allProductListRoute = "allProductListRoute"

fun NavGraphBuilder.allProductListGraph(
    onNavigateToProductDetails: (String) -> Unit
) {
    composable(route = allProductListRoute) {
        val viewModel = hiltViewModel<AllProductListViewModel>()
        val state by viewModel.uiState.collectAsState()
        AllProductListScreen(
            state = state,
            onRefreshScreen = {
                viewModel.loadProductSection()
            },
            onNavigateToProductDetails = { productId ->
                onNavigateToProductDetails(productId)
            }
        )
    }
}

fun NavController.navigateToProductList(navOptions: NavOptions? = null) {
    navigate(allProductListRoute, navOptions)
}

