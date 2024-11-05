package br.com.steventung.movinlist.ui.screens.purchasedproductlist

import br.com.steventung.movinlist.domain.model.Product

data class PurchasedProductListUiState(
    val sections: Map<String, List<Product>> = emptyMap(),
    val searchedText: String = "",
    val onSearchedTextChanged: (String) -> Unit = {}
)