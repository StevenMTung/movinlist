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
import br.com.steventung.movinlist.ui.components.DeleteAlertDialog
import br.com.steventung.movinlist.ui.screens.productdetails.ProductDetailsScreen
import br.com.steventung.movinlist.ui.screens.productdetails.ProductDetailsViewModel

internal const val productDetailsRoute = "productDetailsRoute"
internal const val productIdArgument = "productId"
internal const val productDetailsRouteWithArgument = "$productDetailsRoute/{$productIdArgument}"

fun NavGraphBuilder.productDetailsGraph(
    onNavigateBack: () -> Unit,
    onNavigateToImageDetails: (String) -> Unit
) {
    composable(
        route = productDetailsRouteWithArgument,
        arguments = listOf(navArgument(productIdArgument) { nullable = true })
    ) {
        val viewModel = hiltViewModel<ProductDetailsViewModel>()
        val state by viewModel.uiState.collectAsState()
        val isAddOnPurchasedListSucess by viewModel.isClickButtonSuccess.collectAsState(false)
        val context = LocalContext.current

        LaunchedEffect(isAddOnPurchasedListSucess) {
            if (isAddOnPurchasedListSucess){
                onNavigateBack()
            }
        }

        ProductDetailsScreen(
            state = state,
            onAddProductToPurchasedList = {
                viewModel.addProductToPurchasedList()
            },
            onReturnProductToList = {
                viewModel.returnProductToList()
            },
            onDeleteProductClick = {
                viewModel.setDeleteAlertDialog(true)
            },
            onOpenImage = { imageUrl ->
                onNavigateToImageDetails(imageUrl)
            }
        )

        if(state.isShowDeleteAlertDialog) {
            DeleteAlertDialog(
                title = "Deseja remover o produto?",
                contentText = "Ao confirmar o produto será removido permanentemente da lista.",
                onDismiss = {
                    viewModel.setDeleteAlertDialog(false)
                },
                onConfirm = {
                    viewModel.setDeleteAlertDialog(false)
                    viewModel.removeProduct(context)
                }
            )
        }
    }
}

fun NavController.navigateToProductDetails(productId: String) {
    navigate("$productDetailsRoute/$productId")
}