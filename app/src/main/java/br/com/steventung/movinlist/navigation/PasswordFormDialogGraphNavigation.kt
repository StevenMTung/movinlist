package br.com.steventung.movinlist.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.dialog
import br.com.steventung.movinlist.ui.dialogs.passwordformdialog.PasswordFormDialog
import br.com.steventung.movinlist.ui.dialogs.passwordformdialog.PasswordFormDialogViewModel

internal const val passwordFormDialogGraphRoute = "passwordFormDialogGraphRoute"

fun NavGraphBuilder.passwordFormDialogGraph(
    onDismiss: () -> Unit,
    onDismissAfterChangePassword: () -> Unit
) {
    dialog(route = passwordFormDialogGraphRoute) {
        val viewModel = hiltViewModel<PasswordFormDialogViewModel>()
        val state by viewModel.uiState.collectAsState()
        val isChangePasswordSuccess by viewModel.isChangePasswordSuccess.collectAsState(false)

        LaunchedEffect(isChangePasswordSuccess) {
            if (isChangePasswordSuccess) {
                onDismissAfterChangePassword()
            }
        }
        PasswordFormDialog(
            state = state,
            onDismiss = { onDismiss() },
            onConfirm = {
                viewModel.changePassword()
            },
            onChangeCurrentPasswordVisibility = { currentPasswordVisibility ->
                viewModel.setCurrentPasswordVisibility(currentPasswordVisibility)
            },
            onChangeNewPasswordVisibility = { newPasswordVisibility ->
                viewModel.setNewPasswordVisibility(newPasswordVisibility)
            },
            onChangeNewPasswordVerificationVisibility = { newPasswordVerificationVisibility ->
                viewModel.setNewPasswordVerificationVisibility(newPasswordVerificationVisibility)
            }
        )
    }
}

fun NavController.navigateToPasswordFormDialog() {
    navigate(passwordFormDialogGraphRoute)
}