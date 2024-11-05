package br.com.steventung.movinlist.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.steventung.movinlist.domain.model.Product
import br.com.steventung.movinlist.ui.components.DeleteAlertDialog
import br.com.steventung.movinlist.ui.screens.houseareaproductlist.HouseAreaProductList
import br.com.steventung.movinlist.ui.screens.houseareaproductlist.HouseAreaProductListViewModel

const val houseAreaProductListRoute = "houseAreaProductListRoute"
const val houseAreaProductListArgument = "houseAreaId"
const val houseAreaProductListRouteWithArguments =
    "$houseAreaProductListRoute/{$houseAreaProductListArgument}"

fun NavGraphBuilder.houseAreaProductListGraph(
    onNavigateToProductDetails: (Product) -> Unit,
    onNavigateToHouseAreaForm: (String, String) -> Unit,
    onNavigateBack: () -> Unit
) {
    composable(
        route = houseAreaProductListRouteWithArguments
    ) { backStackEntry ->
        val houseAreaId = backStackEntry.arguments?.getString(houseAreaIdArgument)
        val viewModel = hiltViewModel<HouseAreaProductListViewModel>()
        val state by viewModel.uiState.collectAsState()
        val isDeleteSuccess by viewModel.isDeleteSuccess.collectAsState(false)
        val context = LocalContext.current

        LaunchedEffect(isDeleteSuccess) {
            if(isDeleteSuccess) {
                onNavigateBack()
            }
        }

        HouseAreaProductList(
            state = state,
            onNavigateToProductDetails = { product ->
                onNavigateToProductDetails(product)
            },
            onEditHouseArea = { houseAreaName ->
                houseAreaId?.let { houseAreaId ->
                    onNavigateToHouseAreaForm(houseAreaName, houseAreaId)
                }
            },
            onDeleteHouseArea = {
                viewModel.setDeleteAlertDialog(true)
            }
        )

        if (state.isShowDeleteAlertDialog) {
            DeleteAlertDialog(
                title = "Deseja remover o cômodo?",
                contentText = "Ao confirmar todos os produtos inseridos neste cômodo também serão removidos.",
                onDismiss = {
                    viewModel.setDeleteAlertDialog(false)
                },
                onConfirm = {
                    viewModel.setDeleteAlertDialog(false)
                    viewModel.removeHouseArea(context)
                }
            )
        }
    }
}

fun NavController.navigateToHouseAreaProductList(
    houseAreaId: String,
    navOptions: NavOptions? = null
) {
    navigate("$houseAreaProductListRoute/$houseAreaId", navOptions)
}