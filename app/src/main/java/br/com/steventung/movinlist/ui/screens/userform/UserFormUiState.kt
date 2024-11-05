package br.com.steventung.movinlist.ui.screens.userform

data class UserFormUiState(
    val userId: String = "",
    val userImage: String = "",
    val userName: String = "",
    val onNameChanged: (String) -> Unit = {},
    val isShowModalBottomSheet: Boolean = false,
    val onModalBottomSheetStateChanged: (Boolean) -> Unit = {},
    val onImageChanged: (String) -> Unit = {},
    val isLoading: Boolean = false
)
