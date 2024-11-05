package br.com.steventung.movinlist.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.dialog
import br.com.steventung.movinlist.ui.dialogs.profiledetailsdialog.ProfileDetailsDialog
import br.com.steventung.movinlist.ui.dialogs.profiledetailsdialog.ProfileDetailsDialogViewModel

internal const val profileDetailsDialogRoute = "profileDetailsDialogRoute"

fun NavGraphBuilder.profileDetailsDialogGraph(
    onNavigateBack: () -> Unit,
    onNavigateToUserForm: () -> Unit
) {
    dialog(route = profileDetailsDialogRoute) {
        val viewModel = hiltViewModel<ProfileDetailsDialogViewModel>()
        val state by viewModel.uiState.collectAsState()

        ProfileDetailsDialog(
            state = state,
            onDismiss = {
                onNavigateBack()
            },
            onSignOut = {
                viewModel.signOut()
            },
            onNavigateToUserForm = {
                onNavigateToUserForm()
            }
        )
    }
}

fun NavController.navigateToProfileDetailsDialog() {
    navigate(profileDetailsDialogRoute)
}