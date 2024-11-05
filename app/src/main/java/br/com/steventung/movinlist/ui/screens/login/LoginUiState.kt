package br.com.steventung.movinlist.ui.screens.login

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val onEmailChanged: (String) -> Unit = {},
    val onPasswordChanged: (String) -> Unit = {},
    val error: String? = null,
    val passwordVisibility: Boolean = false
)