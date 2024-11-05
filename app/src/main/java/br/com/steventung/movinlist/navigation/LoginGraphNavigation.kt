package br.com.steventung.movinlist.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.com.steventung.movinlist.ui.screens.login.LoginScreen
import br.com.steventung.movinlist.ui.screens.login.LoginViewModel
import kotlinx.coroutines.launch

internal const val loginGraphRoute = "loginGraphRoute"

fun NavGraphBuilder.loginGraph(
    onNavigateToSignInScreen: () -> Unit
) {
    composable(route = loginGraphRoute) {
        val viewModel = hiltViewModel<LoginViewModel>()
        val state by viewModel.uiState.collectAsState()
        val scope = rememberCoroutineScope()
        LoginScreen(
            state = state,
            onSignIn = {
                scope.launch {
                    viewModel.signIn()
                }
            },
            onNavigateToSignInScreen = {
                onNavigateToSignInScreen()
            },
            onChangePasswordVisibility = { visibility ->
                viewModel.changePasswordVisibility(visibility)
            }
        )
    }
}

fun NavController.navigateToLogin() {
    navigate(loginGraphRoute)
}