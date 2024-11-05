package br.com.steventung.movinlist.domain.model

import java.math.BigDecimal

data class Product(
    val productId: String? = null,
    val name: String = "",
    val description: String = "",
    val image: String = "",
    val brand: String = "",
    val quantity: Int = 0,
    val price: BigDecimal = BigDecimal.ZERO,
    val purchased: Boolean = false,
    val userId: String = "",
    val houseAreaId: String = "",
    val houseAreaName: String = ""
)