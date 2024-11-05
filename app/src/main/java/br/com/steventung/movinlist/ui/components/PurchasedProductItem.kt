package br.com.steventung.movinlist.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.steventung.mobilist.R
import br.com.steventung.movinlist.domain.model.Product
import br.com.steventung.movinlist.extensions.toBrazilianCurrency
import br.com.steventung.movinlist.sample.productSample
import coil.compose.AsyncImage

@Composable
fun PurchasedProductItem(
    modifier: Modifier = Modifier,
    product: Product,
    onPurchasedProductItemClick: () -> Unit = {}
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
            .width(IntrinsicSize.Min)
            .clickable { onPurchasedProductItemClick() }
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            AsyncImage(
                model = product.image,
                contentDescription = "imagem produto comprado",
                modifier = Modifier
                    .height(100.dp)
                    .width(130.dp)
                    .clip(shape = RoundedCornerShape(10.dp)),
                placeholder = painterResource(R.drawable.imagem_padrao),
                error = painterResource(R.drawable.imagem_padrao),
                contentScale = ContentScale.Crop
            )
            Text(
                text = product.name,
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = product.description,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = product.brand,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "Quantidade: " + product.quantity,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = product.price.toBrazilianCurrency(),
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun PurchasedProductItemPreview() {
    PurchasedProductItem(product = productSample)
}