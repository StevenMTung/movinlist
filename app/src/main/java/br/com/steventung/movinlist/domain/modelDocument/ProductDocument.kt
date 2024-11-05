package br.com.steventung.movinlist.domain.modelDocument

import br.com.steventung.movinlist.domain.model.Product
import java.math.BigDecimal
import java.math.RoundingMode

data class ProductDocument(
    val name: String = "",
    val nameLowerCase: String = "",
    val description: String = "",
    val image: String = "",
    val brand: String = "",
    val quantity: Int = 0,
    val price: Double = 0.0,
    val purchased: Boolean = false,
    val userId: String = "",
    val houseAreaId: String = "",
    val houseAreaName: String = ""
) {
    fun toProduct(productId: String) = Product(
        productId = productId,
        name = name,
        description = description,
        image = image,
        brand = brand,
        quantity = quantity,
        price = BigDecimal(price).setScale(2, RoundingMode.HALF_EVEN),
        purchased = purchased,
        userId = userId,
        houseAreaId = houseAreaId,
        houseAreaName = houseAreaName
    )
}