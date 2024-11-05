package br.com.steventung.movinlist

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.com.steventung.mobilist.R
import br.com.steventung.movinlist.navigation.MovinListNavHost
import br.com.steventung.movinlist.navigation.allProductListRoute
import br.com.steventung.movinlist.navigation.budgetRoute
import br.com.steventung.movinlist.navigation.cameraGraphRoute
import br.com.steventung.movinlist.navigation.houseAreaFormRoute
import br.com.steventung.movinlist.navigation.houseAreaFormRouteWithArgument
import br.com.steventung.movinlist.navigation.houseAreaIdArgument
import br.com.steventung.movinlist.navigation.houseAreaListGraphRoute
import br.com.steventung.movinlist.navigation.houseAreaNameArgument
import br.com.steventung.movinlist.navigation.houseAreaProductListRouteWithArguments
import br.com.steventung.movinlist.navigation.imageDetailsRouteWithArgument
import br.com.steventung.movinlist.navigation.loginGraphRoute
import br.com.steventung.movinlist.navigation.navigateSingleTopWithPopUpTo
import br.com.steventung.movinlist.navigation.navigateToHouseAreaForm
import br.com.steventung.movinlist.navigation.navigateToProductForm
import br.com.steventung.movinlist.navigation.navigateToProductFormWithArgument
import br.com.steventung.movinlist.navigation.navigateToProductFormWithProductIdArgument
import br.com.steventung.movinlist.navigation.navigateToProfileDetailsDialog
import br.com.steventung.movinlist.navigation.productDetailsRouteWithArgument
import br.com.steventung.movinlist.navigation.productFormRouteWithArgument
import br.com.steventung.movinlist.navigation.productIdArgument
import br.com.steventung.movinlist.navigation.purchasedProductsRoute
import br.com.steventung.movinlist.navigation.signUpGraphRoute
import br.com.steventung.movinlist.navigation.userFormGraphRoute
import br.com.steventung.movinlist.repository.FirebaseAuthRepository
import br.com.steventung.movinlist.repository.UserRepository
import br.com.steventung.movinlist.ui.components.BottomAppBarItem
import br.com.steventung.movinlist.ui.components.MovinListBottomAppBar
import br.com.steventung.movinlist.ui.components.bottomAppBarItemList
import br.com.steventung.movinlist.ui.theme.MovinListTheme
import coil.compose.AsyncImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var firebaseAuthRepository: FirebaseAuthRepository

    @Inject
    lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MovinListTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    MovinListApp()
                }
            }
        }
    }




    @Composable
    fun MovinListApp(navController: NavHostController = rememberNavController()) {
        val backStackEntryState by navController.currentBackStackEntryAsState()
        val currentDestination = backStackEntryState?.destination
        val currentRoute = currentDestination?.route
        LaunchedEffect(Unit) {
            navController.addOnDestinationChangedListener { _, _, _ ->
                val routes = navController.backQueue.map {
                    it.destination.route
                }
                Log.i("MainActivity", "onCreate: backStack = $routes ")
            }
        }
        val scope = rememberCoroutineScope()

        val snackMessage = navController.currentBackStackEntry
            ?.savedStateHandle
            ?.getStateFlow<String?>("snack_message", null)
            ?.collectAsState()
        backStackEntryState?.savedStateHandle?.remove<String?>("snack_message")
        val snackbarHostState = remember {
            SnackbarHostState()
        }
        snackMessage?.value?.let { message ->
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = message,
                    duration = SnackbarDuration.Long
                )
            }
        }

        val isShowTopAppBar = setTopAppBarVisibility(currentRoute)
        val topAppBarTitle = setTopAppBarTitle(currentRoute, navController)

        val isShowFab = setFabVisibility(currentRoute)
        val fabTitle = setFabTitle(currentRoute)

        val onFabNavigation: () -> Unit = setFabNavigation(currentRoute, navController)

        val bottomAppBarItemSelected by remember(currentRoute) {
            val item = setSelectedBottomAppBarItem(currentRoute)
            mutableStateOf(item)
        }

        val isShowBottomAppBar = setBottomAppBarVisibility(currentRoute)

        val isShowProfilePhoto = setProfilePhotoVisibility(currentRoute)

        val userProfilePhoto = remember(currentRoute?.equals(houseAreaListGraphRoute)) {
            mutableStateOf("")
        }

        LaunchedEffect(currentRoute?.equals(houseAreaListGraphRoute)) {
            val user = firebaseAuthRepository.currentUser.value.currentUser?.email
            user?.let { email ->
                userRepository.getUserImage(email).collect { userImage ->
                    userProfilePhoto.value = userImage
                }
            }
        }

        val isShowBackNavigation = setBackNavigationIconVisibility(currentRoute)

        val isShowEdit = setEditIconVisibility(currentRoute)

        val onEditClick: () -> Unit = {
            editNavigation(navController)
        }

        MovinListApp(
            snackbarHostState = snackbarHostState,
            topBarTitle = topAppBarTitle,
            isShowTopAppBar = isShowTopAppBar,
            isShowFab = isShowFab,
            onFabClick = { onFabNavigation() },
            fabTitle = fabTitle,
            bottomAppBarItemSelected = bottomAppBarItemSelected,
            onBottomAppBarItemChanged = { item ->
                navController.navigateSingleTopWithPopUpTo(item)
            },
            isShowBottomAppBar = isShowBottomAppBar,
            isShowProfilePhoto = isShowProfilePhoto,
            userProfilePhoto = userProfilePhoto.value,
            onNavigateToProfileDetailsDialog = {
                navController.navigateToProfileDetailsDialog()
            },
            isShowBackNavigation = isShowBackNavigation,
            onBackNavigation = {
                navController.navigateUp()
            },
            isShowEdit = isShowEdit,
            onEditClick = onEditClick
        ) {
            MovinListNavHost(navController)
        }
    }

    private fun setTopAppBarTitle(
        currentRoute: String?,
        navController: NavHostController
    ) = when (currentRoute) {
        allProductListRoute -> "Lista Pendentes"
        purchasedProductsRoute -> "Lista Comprados"
        budgetRoute -> "Orçamento"
        houseAreaFormRouteWithArgument -> {
            val houseAreaId = navController.currentBackStackEntry?.savedStateHandle?.get<String>(
                houseAreaIdArgument
            )
            houseAreaId?.let {
                "Editar Cômodo"
            } ?: "Formulário Cômodo"
        }

        productFormRouteWithArgument -> {
            val productId =
                navController.currentBackStackEntry?.savedStateHandle?.get<String>(
                    productIdArgument
                )
            productId?.let {
                "Editar Produto"
            } ?: "Formulário Produto"
        }

        userFormGraphRoute -> "Editar Usuário"
        productDetailsRouteWithArgument -> "Detalhes Produto"

        else -> "MovinList"
    }

    private fun editNavigation(navController: NavHostController) {
        val productId =
            navController.currentBackStackEntry?.savedStateHandle?.get<String>(
                productIdArgument
            )
        val houseAreaId =
            navController.currentBackStackEntry?.savedStateHandle?.get<String>(
                houseAreaIdArgument
            )
        val houseAreName =
            navController.currentBackStackEntry?.savedStateHandle?.get<String>(
                houseAreaNameArgument
            )
        navController.navigateToProductFormWithProductIdArgument(
            productId = productId,
            houseAreaName = houseAreName,
            houseAreaId = houseAreaId
        )
        navController.currentBackStackEntry?.savedStateHandle?.set(productIdArgument, productId)

    }

    private fun setEditIconVisibility(currentRoute: String?) = when (currentRoute) {
        productDetailsRouteWithArgument -> true
        else -> false
    }

    private fun setBackNavigationIconVisibility(currentRoute: String?) = when (currentRoute) {
        houseAreaProductListRouteWithArguments,
        productDetailsRouteWithArgument, houseAreaFormRoute,
        productFormRouteWithArgument, houseAreaFormRouteWithArgument,
        userFormGraphRoute -> true

        else -> false
    }

    private fun setProfilePhotoVisibility(currentRoute: String?) = when (currentRoute) {
        houseAreaListGraphRoute -> true
        else -> false
    }

    private fun setBottomAppBarVisibility(currentRoute: String?) = when (currentRoute) {
        houseAreaListGraphRoute, allProductListRoute,
        purchasedProductsRoute, budgetRoute -> true

        else -> false
    }

    private fun setSelectedBottomAppBarItem(currentRoute: String?) = when (currentRoute) {
        houseAreaListGraphRoute -> BottomAppBarItem.Home
        allProductListRoute -> BottomAppBarItem.ProductList
        purchasedProductsRoute -> BottomAppBarItem.Puchased
        budgetRoute -> BottomAppBarItem.Budget

        else -> BottomAppBarItem.Home
    }

    private fun setFabNavigation(
        currentRoute: String?,
        navController: NavHostController
    ): () -> Unit = {
        when (currentRoute) {
            houseAreaListGraphRoute -> navController.navigateToHouseAreaForm()
            allProductListRoute -> navController.navigateToProductForm()
            houseAreaProductListRouteWithArguments -> {
                val houseAreaId =
                    navController.currentBackStackEntry?.savedStateHandle?.get<String>(
                        houseAreaIdArgument
                    )
                val houseAreaName =
                    navController.currentBackStackEntry?.savedStateHandle?.get<String>(
                        houseAreaNameArgument
                    )
                houseAreaId?.let { areaId ->
                    houseAreaName?.let { areaName ->
                        navController.navigateToProductFormWithArgument(
                            houseAreaId = areaId,
                            houseAreaName = areaName
                        )
                    }
                }
            }

            else -> {}
        }
    }

    private fun setFabTitle(currentRoute: String?) = when (currentRoute) {
        houseAreaListGraphRoute -> "Adicionar cômodo"
        allProductListRoute, houseAreaProductListRouteWithArguments -> "Adicionar produto"
        else -> ""
    }

    private fun setFabVisibility(currentRoute: String?) = when (currentRoute) {
        houseAreaListGraphRoute, allProductListRoute,
        houseAreaProductListRouteWithArguments -> true

        else -> false
    }

    private fun setTopAppBarVisibility(currentRoute: String?) = when (currentRoute) {
        loginGraphRoute, signUpGraphRoute,
        cameraGraphRoute, imageDetailsRouteWithArgument -> false

        else -> true
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovinListApp(
    topBarTitle: String = "",
    isShowTopAppBar: Boolean = false,
    fabTitle: String = "",
    isShowFab: Boolean = false,
    onFabClick: () -> Unit = {},
    onBackNavigation: () -> Unit = {},
    isShowBackNavigation: Boolean = false,
    isShowProfilePhoto: Boolean = false,
    userProfilePhoto: String = "",
    onNavigateToProfileDetailsDialog: () -> Unit = {},
    isShowEdit: Boolean = false,
    onEditClick: () -> Unit = {},
    bottomAppBarItemSelected: BottomAppBarItem = BottomAppBarItem.Home,
    onBottomAppBarItemChanged: (BottomAppBarItem) -> Unit = {},
    isShowBottomAppBar: Boolean = false,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    content: @Composable () -> Unit
) {
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(Modifier.padding(8.dp)) {
                    Text(text = data.visuals.message)
                }
            }
        },
        topBar = {
            if (isShowTopAppBar) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = topBarTitle,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        if (isShowBackNavigation) {
                            IconButton(onClick = { onBackNavigation() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "icone back navigation"
                                )
                            }
                        }
                    },
                    actions = {
                        if (isShowProfilePhoto) {
                            AsyncImage(
                                model = userProfilePhoto,
                                contentDescription = "profile photo",
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(shape = CircleShape)
                                    .clickable { onNavigateToProfileDetailsDialog() },
                                placeholder = painterResource(R.drawable.default_profile_picture),
                                error = painterResource(R.drawable.default_profile_picture),
                                contentScale = ContentScale.Crop
                            )
                        }

                        if (isShowEdit)
                            IconButton(onClick = { onEditClick() }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "ícone edit"
                                )
                            }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colorResource(R.color.purple_500),
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White,
                        actionIconContentColor = Color.White
                    )
                )
            }
        },
        floatingActionButton = {
            if (isShowFab) {
                FloatingActionButton(
                    onClick = { onFabClick() },
                    containerColor = colorResource(R.color.purple_500)
                ) {
                    Row(Modifier.padding(horizontal = 8.dp)) {
                        Text(
                            text = fabTitle,
                            modifier = Modifier.align(Alignment.CenterVertically),
                            color = Color.White
                        )
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "icone fab",
                            modifier = Modifier.align(Alignment.CenterVertically),
                            tint = Color.White
                        )
                    }
                }
            }
        },
        bottomBar = {
            if (isShowBottomAppBar) {
                MovinListBottomAppBar(
                    item = bottomAppBarItemSelected,
                    items = bottomAppBarItemList,
                    onItemChanged = onBottomAppBarItemChanged

                )
            }
        }
    ) { paddingValues ->
        Box(Modifier.padding(paddingValues)) {
            content()
        }
    }
}
