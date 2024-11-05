package br.com.steventung.movinlist.navigation

import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.com.steventung.movinlist.media.persistUriPermission
import br.com.steventung.movinlist.media.verifyPermission
import br.com.steventung.movinlist.ui.components.ModalBottomSheetImageFile
import br.com.steventung.movinlist.ui.screens.productform.ProductFormScreen
import br.com.steventung.movinlist.ui.screens.productform.ProductFormViewModel

internal const val productFormRoute = "productFormRoute"
internal const val houseAreaIdArgument = "houseAreaId"
internal const val houseAreaNameArgument = "houseAreaName"
internal const val productFormRouteWithArgument =
    "$productFormRoute?$houseAreaIdArgument={$houseAreaIdArgument}" +
            "?$houseAreaNameArgument={$houseAreaNameArgument}" +
            "?$productIdArgument={$productIdArgument}"

fun NavGraphBuilder.productFormGraph(
    onBackNavigation: () -> Unit,
    onNavigateToCameraDisplay: () -> Unit,
    onBackNavigationWithSendImageError: () -> Unit
) {
    composable(
        route = productFormRouteWithArgument,
        arguments = listOf(
            navArgument(houseAreaIdArgument) { nullable = true },
            navArgument(houseAreaNameArgument) { nullable = true },
            navArgument(productIdArgument) { nullable = true }
        )
    ) { backStackEntry ->
        val imageUrl = backStackEntry.savedStateHandle.get<String>(imageUriArgument)
        val viewModel = hiltViewModel<ProductFormViewModel>()
        val state by viewModel.uiState.collectAsState()
        val isSaveProductSucess by viewModel.isSaveProductSuccess.collectAsState(false)
        val isFailSendImage by viewModel.isFailSendImage.collectAsState(false)
        val context = LocalContext.current

        LaunchedEffect(imageUrl) {
            imageUrl?.let {
                viewModel.loadImage(it)
            }
        }
        LaunchedEffect(isSaveProductSucess) {
            if (isSaveProductSucess) {
                onBackNavigation()
            }
        }
        LaunchedEffect(isFailSendImage) {
            if (isFailSendImage) {
                if (state.image.startsWith("http")) {
                    onBackNavigation()
                } else {
                    onBackNavigationWithSendImageError()
                }
            }
        }

        ProductFormScreen(
            state = state,
            onIncreaseQuantity = {
                viewModel.increaseQuantity()
            },
            onDecreaseQuantity = {
                viewModel.decreaseQuantity()
            },
            onExpandDropDownMenu = {
                viewModel.setDropDownMenuExpandedState()
            },
            onDismissDropDownMenu = {
                viewModel.setDropDownMenuExpandedState()
            },
            onSelectedDropDownMenuItem = { index ->
                viewModel.selectedDropDownMenuItem(index)
            },
            onClickImage = {
                viewModel.setModalBottomSheet(true)
            },
            onClickSave = {
                viewModel.saveProduct(context)
            }
        )

        val pickMedia = rememberLauncherForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            if (uri != null) {
                context.persistUriPermission(uri)
                viewModel.loadImage(uri.toString())
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }
        val requestCameraPermission = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { granted ->
                if (granted) {
                    onNavigateToCameraDisplay()
                } else {
                    Log.d("Camera", "Permission to access camera not granted")
                }
            }
        )

        if (state.isShowModalBottomSheet) {
            ModalBottomSheetImageFile(
                onDismiss = { viewModel.setModalBottomSheet(false) },
                onClickPhotoAlbum = {
                    pickMedia.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                    viewModel.setModalBottomSheet(false)
                },
                onClickCamera = {
                    if (context.verifyPermission(Manifest.permission.CAMERA)) {
                        requestCameraPermission.launch(Manifest.permission.CAMERA)
                    } else {
                        onNavigateToCameraDisplay()
                    }
                    viewModel.setModalBottomSheet(false)
                }
            )
        }
    }
}


fun NavController.navigateToProductForm() {
    navigate(productFormRoute)
}

fun NavController.navigateToProductFormWithArgument(
    houseAreaId: String?,
    houseAreaName: String?,
) {
    navigate(
        productFormRoute +
                "?$houseAreaIdArgument=$houseAreaId" +
                "?$houseAreaNameArgument=$houseAreaName" +
                "?$productIdArgument={$productIdArgument}"
    )
}

fun NavController.navigateToProductFormWithProductIdArgument(
    houseAreaId: String?,
    houseAreaName: String?,
    productId: String?
) {
    navigate(
        productFormRoute +
                "?$houseAreaIdArgument=$houseAreaId" +
                "?$houseAreaNameArgument=$houseAreaName" +
                "?$productIdArgument=$productId"
    )
}
