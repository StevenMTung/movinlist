package br.com.steventung.movinlist.ui.screens.allproductlist

import br.com.steventung.movinlist.domain.model.Product

data class AllProductListUiState(
    val sections: Map<String, List<Product>> = emptyMap(),
    val searchedText: String = "",
    val onSearchedText: (String) -> Unit = {}
)