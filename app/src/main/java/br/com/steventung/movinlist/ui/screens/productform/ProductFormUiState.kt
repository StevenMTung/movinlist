package br.com.steventung.movinlist.ui.screens.productform

data class ProductFormUiState(
    val image: String = "",
    val name: String = "",
    val brand: String = "",
    val description: String = "",
    val quantity: Int = 1,
    val price: String = "",
    val dropDownValue: String = "",
    val isDropDownExpanded: Boolean = false,
    val houseAreaList: List<String> = emptyList(),
    val isPurchased: Boolean = false,
    val onLoadImage: (String) -> Unit = {},
    val onNameChanged: (String) -> Unit = {},
    val onBrandChanged: (String) -> Unit = {},
    val onPriceChanged: (String) -> Unit = {},
    val onDescriptionChanged: (String) -> Unit = {},
    val onExpandedDropDownMenu: (Boolean) -> Unit = {},
    val onDropDownMenuValueChanged: (String) -> Unit = {},
    val isShowModalBottomSheet: Boolean = false,
    val isShowDropDownMenu: Boolean = false,
    val emptyHouseAreaNameError: Boolean = false,
    val isLoading: Boolean = false
)