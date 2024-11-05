package br.com.steventung.movinlist.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.com.steventung.movinlist.ui.screens.houseareaform.HouseAreaFormScreen
import br.com.steventung.movinlist.ui.screens.houseareaform.HouseAreaFormViewModel

internal const val houseAreaFormRoute = "houseAreaFormRoute"
internal const val houseAreaFormRouteWithArgument =
    "$houseAreaFormRoute?$houseAreaNameArgument={$houseAreaNameArgument}?$houseAreaIdArgument={$houseAreaIdArgument}"

fun NavGraphBuilder.houseAreaFormGraph(
    onNavigateBack: () -> Unit = {},
    onNavigateToHouseAreaProductList: (String) -> Unit = {}
) {
    composable(
        route = houseAreaFormRouteWithArgument,
        arguments = listOf(
            navArgument(houseAreaNameArgument) { nullable = true },
            navArgument(houseAreaIdArgument) { nullable = true }
        )
    ) { backStackEntry ->
        val houseAreaId = backStackEntry.arguments?.getString(houseAreaIdArgument)
        val viewModel = hiltViewModel<HouseAreaFormViewModel>()
        val state by viewModel.uiState.collectAsState()
        val isSavesSuccess by viewModel.isSaveSuccess.collectAsState("")
        val context = LocalContext.current
        LaunchedEffect(isSavesSuccess) {
            if (isSavesSuccess.isNotEmpty()) {
                houseAreaId?.let {
                    onNavigateToHouseAreaProductList(isSavesSuccess)
                } ?: onNavigateBack()
            }
        }

        HouseAreaFormScreen(
            state = state,
            onCreateHouseArea = {
                viewModel.saveHouseArea(context)
            }
        )
    }
}

fun NavController.navigateToHouseAreaForm() {
    navigate(houseAreaFormRoute)
}

fun NavController.navigateToHouseAreaFormWithArgument(
    houseAreaName: String,
    houseAreaId: String
) {
    navigate("$houseAreaFormRoute?$houseAreaNameArgument=$houseAreaName?$houseAreaIdArgument=$houseAreaId")
}