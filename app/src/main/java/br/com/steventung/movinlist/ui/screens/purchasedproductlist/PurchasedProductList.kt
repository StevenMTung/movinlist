package br.com.steventung.movinlist.ui.screens.purchasedproductlist

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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import br.com.steventung.mobilist.R
import br.com.steventung.movinlist.sample.houseAreaProductsSectionSample
import br.com.steventung.movinlist.ui.components.PurchaseProductSection

@Composable
fun PurchasedProductListScreen(
    modifier: Modifier = Modifier,
    state: PurchasedProductListUiState,
    onPurchasedProductItemClick: (String) -> Unit = {},
    onRefreshScreen: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.purchased_background))
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = state.searchedText,
                onValueChange = { state.onSearchedTextChanged(it) },
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
                    imeAction = ImeAction.Done,
                    capitalization = KeyboardCapitalization.Words
                )
            )
        }
        val sections = state.sections
        if (sections.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Não foram encontrados produtos comprados",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                TextButton(
                    onClick = { onRefreshScreen() },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "ícone refresh"
                    )
                    Text(text = "Recarregar tela")
                }

            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                sections.forEach { section ->
                    val title = section.key
                    val productList = section.value
                    item {
                        PurchaseProductSection(
                            title = title,
                            productList = productList,
                            onPurchasedProductItemClick = onPurchasedProductItemClick
                        )
                    }
                }

            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun PurchasedProductListScreenPreview() {
    PurchasedProductListScreen(
        state = PurchasedProductListUiState(sections = houseAreaProductsSectionSample)
    )
}

@Preview(showSystemUi = true)
@Composable
private fun PurchasedProductListScreenEmptyPreview() {
    PurchasedProductListScreen(
        state = PurchasedProductListUiState(sections = emptyMap())
    )
}