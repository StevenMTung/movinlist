package br.com.steventung.movinlist.ui.dialogs.deleteaccountdialog

data class DeleteAccountDialogUiState(
    val email: String = "",
    val password: String = "",
    val onPasswordChanged: (String) -> Unit = {},
    val passwordVisibility: Boolean = false,
    val onChangePasswordVisibilityChanged: (Boolean) -> Unit = {},
    val isDeleteLoading: Boolean = false,
    val errorMessage: String? = null
)

