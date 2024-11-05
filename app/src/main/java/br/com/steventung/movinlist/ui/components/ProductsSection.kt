package br.com.steventung.movinlist.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
fun ProductsSection(
    modifier: Modifier = Modifier,
    title: String,
    productList: List<Product>,
    onProductCardClick: (String) -> Unit = {}
) {
    Column(modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = colorResource(R.color.product_section_title))
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp),
                color = Color.DarkGray,
                fontWeight = FontWeight(500)
            )
        }
        Column(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            productList.forEach { product ->
                ProductItem(
                    product = product,
                    onProductCardClick = {
                        product.productId?.let { onProductCardClick(it) }
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductsSectionPreview() {
    ProductsSection(
        title = "Sala",
        productList = productListSample
    )
}