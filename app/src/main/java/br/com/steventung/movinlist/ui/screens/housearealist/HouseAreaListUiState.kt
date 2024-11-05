package br.com.steventung.movinlist.ui.screens.housearealist

import br.com.steventung.movinlist.domain.model.HouseArea

data class HouseAreaListUiState(
    val houseAreas: List<HouseArea> = emptyList(),
    val isLogged: Boolean = false
)