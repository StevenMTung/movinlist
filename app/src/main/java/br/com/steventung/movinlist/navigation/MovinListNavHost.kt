package br.com.steventung.movinlist.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import br.com.steventung.movinlist.ui.appstate.AppStateUiState
import br.com.steventung.movinlist.ui.appstate.AppStateViewModel
import br.com.steventung.movinlist.ui.components.BottomAppBarItem


@Composable
fun MovinListNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = splashScreenGraphRoute
    ) {
        splashScreenGraph()
        loginGraph(
            onNavigateToSignInScreen = {
                navController.navigateToSignUp()
            }
        )
        signUpGraph(
            onNavigateBack = { navController.navigateUp() }
        )
        houseAreaListGraph(
            onNavigateToHouseAreaProductList = { houseArea ->
                houseArea.houseAreaId?.let {
                    navController.navigateToHouseAreaProductList(it)
                    navController.currentBackStackEntry?.savedStateHandle?.apply {
                        set(houseAreaIdArgument, it)
                        set(houseAreaNameArgument, houseArea.houseAreaName)
                    }
                }
            }
        )
        allProductListGraph(
            onNavigateToProductDetails = { productId ->
                navController.navigateToProductDetails(productId = productId)
                navController.currentBackStackEntry?.savedStateHandle?.set(
                    productIdArgument,
                    productId
                )
            }
        )
        purchasedProductListGraph(
            onNavigateToProductDetails = { productId ->
                navController.navigateToProductDetails(productId = productId)
                navController.currentBackStackEntry?.savedStateHandle?.set(
                    productIdArgument,
                    productId
                )
            }
        )
        budgetgraph()
        houseAreaFormGraph(
            onNavigateBack = {
                navController.navigateUp()
            },
            onNavigateToHouseAreaProductList = { houseAreaId ->
                navController.navigateToHouseAreaProductList(
                    houseAreaId = houseAreaId,
                    navOptions = navOptions {
                        popUpTo(houseAreaProductListRouteWithArguments) { inclusive = true }
                    }
                )
            }
        )
        productFormGraph(
            onBackNavigation = {
                navController.navigateUp()
            },
            onNavigateToCameraDisplay = {
                navController.navigateToCameraDisplay()
            },
            onBackNavigationWithSendImageError = {
                navController.popBackStack()
                navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.set("snack_message", "Falha na conexão de rede: Produto salvo sem imagem")
            }
        )
        houseAreaProductListGraph(
            onNavigateToProductDetails = { product ->
                product.productId?.let { navController.navigateToProductDetails(it) }
                navController.currentBackStackEntry?.savedStateHandle?.apply {
                    set(productIdArgument, product.productId)
                    set(houseAreaIdArgument, product.houseAreaId)
                    set(houseAreaNameArgument, product.houseAreaName)
                }
            },
            onNavigateToHouseAreaForm = { houseAreaName, houseAreaId ->
                navController.navigateToHouseAreaFormWithArgument(houseAreaName, houseAreaId)
                navController.currentBackStackEntry?.savedStateHandle?.set(houseAreaIdArgument, houseAreaId)
            },
            onNavigateBack = {
                navController.navigateUp()
            }
        )
        productDetailsGraph(
            onNavigateBack = {
                navController.navigateUp()
            },
            onNavigateToImageDetails = { imageUrl ->
                navController.navigateToImageDetails(imageUrl)
            }
        )
        profileDetailsDialogGraph(
            onNavigateBack = {
                navController.navigateUp()
            },
            onNavigateToUserForm = {
                navController.navigateToUserForm()
            }
        )
        userFormGraph(
            onNavigateBack = {
                navController.navigateUp()
            },
            onNavigateToPasswordFormDialog = {
                navController.navigateToPasswordFormDialog()
            },
            onNavigateToDeletePasswordDialog = {
                navController.navigateToDeleteAccountDialog()
            },
            onNavigateToCameraDisplay = {
                navController.navigateToCameraDisplay()
            },
            onNavigateBackWithSendImageError = {
                navController.popBackStack()
                navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.set("snack_message", "Falha na conexão de rede: Usuário salvo sem imagem")

            }
        )
        passwordFormDialogGraph(
            onDismiss = {
                navController.navigateUp()
            },
            onDismissAfterChangePassword = {
                navController.navigateUp()
                navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.set("snack_message", "✔ Senha alterada com sucesso")
            }
        )
        deleteAccountDialogGraph(
            onNavigateBack = {
                navController.navigateUp()
            }
        )
        cameraGraph(
            onNavigateBack = { navController.navigateUp() },
            onPhotoTaken = { imageUrl ->
                navController.previousBackStackEntry?.savedStateHandle?.set(imageUriArgument, imageUrl)
                navController.popBackStack()
            }
        )
        imageDetailsGraph(
            onNavigateBack = {
                navController.popBackStack()
            }
        )
    }

    val appViewModel = hiltViewModel<AppStateViewModel>()
    val appState by appViewModel.appState.collectAsState(AppStateUiState())
    val userVerification: MutableState<String?> = rememberSaveable {
        mutableStateOf("")
    }
    LaunchedEffect(appState) {
        if (appState.isInitLoading) {
            return@LaunchedEffect
        }
        if (userVerification.value != appState.user?.userId) {
            appState.user?.let {
                navController.navigateClearBackStack(houseAreaListGraphRoute)
            } ?: navController.navigateClearBackStack(loginGraphRoute)
            userVerification.value = appState.user?.userId
        }
    }
}

fun NavController.navigateClearBackStack(route: String) {
    navigate(route) {
        popUpTo(0) { inclusive = true }
    }
}

fun NavController.navigateSingleTopWithPopUpTo(item: BottomAppBarItem) {
    val (route, navigate) = when (item) {
        BottomAppBarItem.Home -> Pair(
            houseAreaListGraphRoute,
            ::navigateToHouseAreaList
        )

        BottomAppBarItem.ProductList -> Pair(
            allProductListRoute,
            ::navigateToProductList
        )

        BottomAppBarItem.Puchased -> Pair(
            purchasedProductsRoute,
            ::navigateToPurchasedProducts
        )

        BottomAppBarItem.Budget -> Pair(
            budgetRoute,
            ::navigateToBudget
        )
    }
    val navOptions = navOptions {
        launchSingleTop = true
        popUpTo(route)
    }
    navigate(navOptions)
}