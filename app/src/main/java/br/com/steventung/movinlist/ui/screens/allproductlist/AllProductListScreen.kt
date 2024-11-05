package br.com.steventung.movinlist.ui.screens.allproductlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import br.com.steventung.movinlist.sample.houseAreaProductsSectionSample
import br.com.steventung.movinlist.ui.components.ProductsSection

@Composable
fun AllProductListScreen(
    modifier: Modifier = Modifier,
    state: AllProductListUiState,
    onRefreshScreen: () -> Unit = {},
    onNavigateToProductDetails: (String) -> Unit = {}
) {
    Column(modifier = modifier) {
        Box(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = state.searchedText,
                onValueChange = {
                    state.onSearchedText(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "ícone pesquisa"
                    )
                },
                label = { Text(text = "Pesquisar produto") },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Done
                ),
                maxLines = 1,

            )
        }
        val sections = state.sections
        if (sections.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Não foram encontrados produtos cadastrados",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                TextButton(
                    onClick = { onRefreshScreen() },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = "icone refresh")
                    Text(text = "Recarregar tela")
                }
            }
        } else {
            LazyColumn(modifier.fillMaxSize()) {
                for (section in sections) {
                    val title = section.key
                    val productList = section.value
                    item {
                        ProductsSection(
                            title = title,
                            productList = productList,
                            onProductCardClick = onNavigateToProductDetails
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AllProductListEmptyPreview() {
    AllProductListScreen(
        state = AllProductListUiState(
            sections = emptyMap()
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun AllProductListPreview() {
    AllProductListScreen(
        state = AllProductListUiState(
            sections = houseAreaProductsSectionSample
        )
    )
}