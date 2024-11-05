package br.com.steventung.movinlist.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.com.steventung.movinlist.ui.screens.imagedetails.ImageDetailsScreen
import br.com.steventung.movinlist.ui.screens.imagedetails.ImageDetailsViewModel
import java.net.URLEncoder

internal const val imageDetailsRoute = "imageDetailsRoute"
internal const val imageDetailsRouteWithArgument = "$imageDetailsRoute/{$imageUriArgument}"

fun NavGraphBuilder.imageDetailsGraph(
    onNavigateBack: () -> Unit
) {
    composable(
        route = imageDetailsRouteWithArgument,
        arguments = listOf(navArgument(imageUriArgument) { nullable = true })
    ) {
        val viewModel = hiltViewModel<ImageDetailsViewModel>()
        val state by viewModel.uiState.collectAsState()
        ImageDetailsScreen(
            state = state,
            onBackNavigation = {
                onNavigateBack()
            }
        )
    }
}

fun NavController.navigateToImageDetails(imageUrl: String) {
    val encodedUrl = URLEncoder.encode(imageUrl, "UTF-8")
    navigate("$imageDetailsRoute/$encodedUrl")
}
