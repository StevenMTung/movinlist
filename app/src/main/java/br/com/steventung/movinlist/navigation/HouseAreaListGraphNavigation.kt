package br.com.steventung.movinlist.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.steventung.movinlist.domain.model.HouseArea
import br.com.steventung.movinlist.ui.screens.housearealist.HouseAreaListScreen
import br.com.steventung.movinlist.ui.screens.housearealist.HouseAreaListViewModel

internal const val houseAreaListGraphRoute = "houseAreaListGraphRoute"

fun NavGraphBuilder.houseAreaListGraph(
    onNavigateToHouseAreaProductList: (HouseArea) -> Unit
) {
    composable(route = houseAreaListGraphRoute) {
        val viewModel = hiltViewModel<HouseAreaListViewModel>()
        val state by viewModel.uiState.collectAsState()
        HouseAreaListScreen(
            state = state,
            onNavigateToHouseAreaProductList = { houseArea ->
                onNavigateToHouseAreaProductList(houseArea)
            }
        )
    }
}

fun NavController.navigateToHouseAreaList(navOptions: NavOptions? = null) {
    navigate(houseAreaListGraphRoute, navOptions)
}

fun NavController.navigateToHouseAreaListClearBackStack() {
    navigate(houseAreaListGraphRoute) {
        popUpTo(0) { inclusive = true }
    }
}