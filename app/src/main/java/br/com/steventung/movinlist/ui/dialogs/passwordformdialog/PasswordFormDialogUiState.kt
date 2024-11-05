package br.com.steventung.movinlist.ui.dialogs.passwordformdialog

data class PasswordFormDialogUiState(
    val currentPassword: String = "",
    val newPassword: String = "",
    val newPasswordVerification: String = "",
    val onCurrentPasswordChanged: (String) -> Unit = {},
    val onNewPasswordChanged: (String) -> Unit = {},
    val onNewPasswordVerificationChanged: (String) -> Unit = {},
    val passwordErrorMessage: String? = null,
    val newPasswordVerificationError: String? = null,
    val isShowCurrentPassword: Boolean = false,
    val isShowNewPassword: Boolean = false,
    val isShowNewPasswordVerification: Boolean = false,
    val onCurrentPasswordVisibilityChanged: (Boolean) -> Unit = {},
    val onNewPasswordVisibilityChanged: (Boolean) -> Unit = {},
    val onNewPasswordVerificationVisibilityChanged: (Boolean) -> Unit = {},
    val isLoading: Boolean = false
)