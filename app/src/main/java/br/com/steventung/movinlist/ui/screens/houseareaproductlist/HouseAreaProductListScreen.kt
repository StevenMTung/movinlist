package br.com.steventung.movinlist.ui.screens.houseareaproductlist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.steventung.mobilist.R
import br.com.steventung.movinlist.domain.model.Product
import br.com.steventung.movinlist.sample.productListSample
import br.com.steventung.movinlist.ui.components.ProductItem

@Composable
fun HouseAreaProductList(
    modifier: Modifier = Modifier,
    state: HouseAreaProductListUiState,
    onNavigateToProductDetails: (Product) -> Unit = {},
    onEditHouseArea: (String) -> Unit = {},
    onDeleteHouseArea: () -> Unit = {}
) {
    Column {
        AnimatedVisibility(!state.deleteErrorMessage.isNullOrBlank()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.Red)
            ) {
                state.deleteErrorMessage?.let {
                    Text(
                        text = it,
                        modifier = Modifier.padding(8.dp),
                        color = Color.White
                    )
                }
            }
        }
        Column(modifier = modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = colorResource(R.color.product_section_title))
            ) {
                Row {
                    Text(
                        text = "Lista " + state.houseAreaName,
                        modifier = Modifier
                            .padding(vertical = 4.dp, horizontal = 16.dp)
                            .align(Alignment.CenterVertically)
                            .weight(1f),
                        fontSize = 18.sp,
                        fontWeight = FontWeight(500),
                        color = Color.DarkGray
                    )
                    IconButton(
                        onClick = { onEditHouseArea(state.houseAreaName) },
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "ícone edição"
                        )
                    }
                    IconButton(
                        onClick = { onDeleteHouseArea() },
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "ícone remove"
                        )
                    }
                }
            }
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(state.productList) { product ->
                    ProductItem(
                        product = product,
                        onProductCardClick = {
                            onNavigateToProductDetails(product)
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HouseAreaProductListPreview() {
    HouseAreaProductList(
        state = HouseAreaProductListUiState(
            houseAreaName = "Sala",
            productList = productListSample
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun HouseAreaProductListWithDeleteErrorPreview() {
    HouseAreaProductList(
        state = HouseAreaProductListUiState(
            houseAreaName = "Sala",
            productList = productListSample,
            deleteErrorMessage = "Erro: Falha ao remover cômodo, verifique a conexão de rede"
        )
    )
}