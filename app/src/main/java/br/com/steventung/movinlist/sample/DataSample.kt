package br.com.steventung.movinlist.sample

import br.com.steventung.movinlist.domain.model.HouseArea
import br.com.steventung.movinlist.domain.model.Product
import java.math.BigDecimal

val houseAreaSample = listOf<HouseArea>(
    HouseArea(houseAreaName = "Sala"),
    HouseArea(houseAreaName = "Cozinha"),
    HouseArea(houseAreaName = "Varanda"),
    HouseArea(houseAreaName = "Banheiro"),
    HouseArea(houseAreaName = "Quarto"),
)

val productSample = Product(
    name = "Tv",
    description = "Televisão de 50 polegadas",
    brand = "Samsung",
    image = "/storage/self/primary/Download/TV.jpg",
    quantity = 1,
    price = BigDecimal("5000")
)

val productListSample = listOf(
    Product(
        name = "Tv",
        description = "Televisão de 50 polegadas",
        brand = "Samsung",
        image = "/storage/self/primary/Download/TV.jpg",
        quantity = 1,
        price = BigDecimal("5000"),
    ),
    Product(
        name = "Sofa",
        description = "Medidas: 120 x 80",
        brand = "Komfort House",
        image = "/storage/self/primary/Download/Sofa.jpg",
        quantity = 2,
        price = BigDecimal("3000")
    ),
    Product(
        name = "Home Theater",
        description = "Com caixas de som e subwoofer",
        brand = "Sony",
        image = "/storage/self/primary/Download/HomeTheater.png",
        quantity = 1,
        price = BigDecimal("3400")
    ),
    Product(
        name = "Almofadas",
        description = "Cor creme e verde",
        brand = "Foom Store",
        image = "/storage/self/primary/Download/Almofadas Sala.jpg",
        quantity = 4,
        price = BigDecimal("300")
    )
)

val houseAreaProductsSectionSample = mapOf(
    "Sala" to productListSample,
    "Cozinha" to productListSample,
    "Quarto" to productListSample
)

val productListTest = listOf(
    Product(
        name = "Sofa",
        description = "teste",
        brand = "teste",
        price = BigDecimal("2000"),
        houseAreaName = "Sala",
        quantity = 2
    ),
    Product(
        name = "Tv",
        description = "teste",
        brand = "teste",
        price = BigDecimal("5000"),
        houseAreaName = "Sala",
        quantity = 1
    ),
    Product(
        name = "Liquidificador",
        description = "teste",
        brand = "teste",
        price = BigDecimal("750"),
        houseAreaName = "Cozinha",
        quantity = 2
    ),
    Product(
        name = "Cama Box",
        description = "teste",
        brand = "teste",
        price = BigDecimal("2000"),
        houseAreaName = "Quarto",
        quantity = 3
    ),
    Product(
        name = "Criado Mudo",
        description = "teste",
        brand = "teste",
        price = BigDecimal("600"),
        houseAreaName = "Quarto",
        quantity = 2
    )
)


