package br.com.steventung.movinlist.ui.screens.houseareaform

data class HouseAreaFormUiState(
    val houseAreaName: String = "",
    val isShowErrorMessage: Boolean = false,
    val isLoading: Boolean = false,
    val isShowConflictError: Boolean = false,
    val onHouseAreaNameChanged: (String) -> Unit = {},
    val networkErrorMessage: String? = null
)