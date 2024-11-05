package br.com.steventung.movinlist.domain.modelDocument

import br.com.steventung.movinlist.domain.model.HouseArea

data class HouseAreaDocument(
    val houseAreaName: String = "",
    val userId: String = ""
) {
    fun toHouseArea(houseAreaId: String) = HouseArea(
        houseAreaId = houseAreaId,
        houseAreaName = houseAreaName,
        userId = userId
    )
}
