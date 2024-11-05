package br.com.steventung.movinlist.ui.screens.housearealist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.steventung.movinlist.domain.model.HouseArea
import br.com.steventung.movinlist.sample.houseAreaSample
import br.com.steventung.movinlist.ui.components.HouseAreaItemCard

@Composable
fun HouseAreaListScreen(
    state: HouseAreaListUiState,
    onNavigateToHouseAreaProductList: (HouseArea) -> Unit = {},
) {
    Column(Modifier.fillMaxSize()) {
        if (state.houseAreas.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = "Lista de cômodos vazia",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(state.houseAreas) { houseArea ->
                    HouseAreaItemCard(
                        houseArea,
                        onClickHouseAreaCard = {
                            houseArea.houseAreaId?.let {
                                onNavigateToHouseAreaProductList(houseArea)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HouseAreaListScreenPreview() {
    HouseAreaListScreen(
        HouseAreaListUiState(houseAreas = houseAreaSample)
    )
}

@Preview(showBackground = true)
@Composable
private fun HouseAreaListScreenEmptyPreview() {
    HouseAreaListScreen(
        HouseAreaListUiState(houseAreas = emptyList())
    )
}