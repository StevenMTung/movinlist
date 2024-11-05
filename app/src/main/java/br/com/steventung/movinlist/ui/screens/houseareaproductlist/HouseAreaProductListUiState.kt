package br.com.steventung.movinlist.ui.screens.houseareaproductlist

import br.com.steventung.movinlist.domain.model.Product

data class HouseAreaProductListUiState(
    val houseAreaName: String = "",
    val productList: List<Product> = emptyList(),
    val isShowDeleteAlertDialog: Boolean = false,
    val onDeleteAlertDialogStateChanged: (Boolean) -> Unit = {},
    val deleteErrorMessage: String? = null
)
