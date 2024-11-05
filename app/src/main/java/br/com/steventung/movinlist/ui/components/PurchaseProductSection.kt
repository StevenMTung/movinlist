package br.com.steventung.movinlist.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@Composable
fun PurchaseProductSection(
    modifier: Modifier = Modifier,
    title: String,
    productList: List<Product>,
    onPurchasedProductItemClick: (String) -> Unit = {}
) {
    Column(
        modifier = modifier
            .background(color = colorResource(R.color.purchased_background))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = colorResource(R.color.purchased_section_title))
        ) {
            Text(
                text = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                fontWeight = FontWeight(500),
                color = Color.DarkGray,
                fontSize = 18.sp
                )
        }
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
        ) {
            items(productList) { product ->
                PurchasedProductItem(
                    product = product,
                    onPurchasedProductItemClick = {
                        product.productId?.let { onPurchasedProductItemClick(it) }
                    }
                )
            }
        }
        HorizontalDivider()
    }
}

@Preview(showBackground = true)
@Composable
private fun PurchaseProductSectionPreview() {
    PurchaseProductSection(
        title = "Sala",
        productList = productListSample
    )
}