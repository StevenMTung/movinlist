package br.com.steventung.movinlist.ui.screens.signup

data class SignUpUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val passwordVerify: String = "",
    val profilePhoto: String = "",
    val passwordVisibility: Boolean = false,
    val passwordVerifyVisibility: Boolean = false,
    val onPasswordVisibilityChanged: (Boolean) -> Unit = {},
    val onPasswordVerifyVisibilityChanged: (Boolean) -> Unit = {},
    val onNameChanged: (String) -> Unit = {},
    val onEmailChanged: (String) -> Unit = {},
    val onPasswordChanged: (String) -> Unit = {},
    val onPasswordVerifyChanged: (String) -> Unit = {},
    val onProfilePhotoChanged: (String) -> Unit = {},
    val error: String? = null,
    val passwordVerifyErrorMessage: String? = null,
    val isShowModalBottomSheet: Boolean = false,
    val onModalBottomSheetStateChanged: (Boolean) -> Unit = {},
    val isCameraOpen: Boolean = false,
    val onCameraOpenStateChanged: (Boolean) -> Unit = {}
)