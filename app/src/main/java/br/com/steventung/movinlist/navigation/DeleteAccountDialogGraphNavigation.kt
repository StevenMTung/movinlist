package br.com.steventung.movinlist.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.dialog
import br.com.steventung.movinlist.ui.dialogs.deleteaccountdialog.DeleteAccountDialog
import br.com.steventung.movinlist.ui.dialogs.deleteaccountdialog.DeleteAccountDialogViewModel

internal const val deleteAccountDialogRoute = "deleteAccountDialogRoute"

fun NavGraphBuilder.deleteAccountDialogGraph(
    onNavigateBack: () -> Unit
) {
    dialog(route = deleteAccountDialogRoute) {
        val viewModel = hiltViewModel<DeleteAccountDialogViewModel>()
        val state by viewModel.uiState.collectAsState()

        DeleteAccountDialog(
            state = state,
            onDismiss = { onNavigateBack() },
            onConfirm = {
                viewModel.deleteUserAccount()
            },
            onChangePasswordVisibility = { passwordVisibility ->
                viewModel.setPasswordVisibility(passwordVisibility)
            }
        )
    }
}

fun NavController.navigateToDeleteAccountDialog() {
    navigate(deleteAccountDialogRoute)
}