package br.com.steventung.movinlist.ui.screens.productdetails

import java.math.BigDecimal

data class ProductDetailsUiState(
    val image: String = "",
    val name: String = "",
    val brand: String = "",
    val description: String = "",
    val price: BigDecimal = BigDecimal.ZERO,
    val quantity: String = "",
    val houseAreaName: String = "",
    val purchased: Boolean = false,
    val isShowDeleteAlertDialog: Boolean = false,
    val onDeleteAlertDialogStateChanged: (Boolean) -> Unit = {},
    val isLoading: Boolean = false,
    val deleteErrorMessage: String? = null
)
