package br.com.steventung.movinlist.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.steventung.movinlist.domain.model.HouseArea

@Composable
fun HouseAreaItemCard(
    houseArea: HouseArea,
    modifier: Modifier = Modifier,
    elevation: Dp = 0.dp,
    onClickHouseAreaCard: () -> Unit = {}
) {
    Card(
        modifier
            .fillMaxWidth()
            .clickable { onClickHouseAreaCard() },
        elevation = CardDefaults.cardElevation(elevation)
    ) {
        Text(
            text = houseArea.houseAreaName,
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.CenterHorizontally),
            fontWeight = FontWeight(500),
            fontSize = 18.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
private fun HouseAreaItemCardPreview() {
    HouseAreaItemCard(HouseArea(houseAreaName = "Sala"))
}
