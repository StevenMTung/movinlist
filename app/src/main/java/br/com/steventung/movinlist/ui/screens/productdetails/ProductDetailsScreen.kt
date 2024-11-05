package br.com.steventung.movinlist.ui.screens.productdetails

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.steventung.mobilist.R
import br.com.steventung.movinlist.extensions.toBrazilianCurrency
import coil.compose.AsyncImage
import java.math.BigDecimal

@Composable
fun ProductDetailsScreen(
    modifier: Modifier = Modifier,
    state: ProductDetailsUiState,
    onAddProductToPurchasedList: () -> Unit = {},
    onReturnProductToList: () -> Unit = {},
    onDeleteProductClick: () -> Unit = {},
    onOpenImage: (String) -> Unit = {}
) {
    if (state.isLoading) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            Text(
                text = "Carregando...",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    } else {
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
                            modifier = Modifier.fillMaxWidth().padding(8.dp),
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AsyncImage(
                    model = state.image,
                    contentDescription = "imagem produto",
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 250.dp, max = 400.dp)
                        .clickable {
                            if (state.image.isNotEmpty()) {
                                onOpenImage(state.image)
                            }
                        },
                    contentScale = ContentScale.Fit,
                    placeholder = painterResource(R.drawable.imagem_padrao),
                    error = painterResource(R.drawable.imagem_padrao)
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ProductFields(title = "Produto", content = state.name)
                    ProductFields(title = "Marca", content = state.brand)
                    ProductFields(title = "Descrição", content = state.description)
                    ProductFields(
                        title = "Preço Unitário",
                        content = state.price.toBrazilianCurrency()
                    )
                    ProductFields(title = "Quantidade", content = state.quantity)
                    ProductFields(title = "Cômodo", content = state.houseAreaName)
                    ProductFields(
                        title = "Status", content = if (state.purchased) {
                            "Comprado"
                        } else {
                            "Pendente"
                        }
                    )
                }
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (state.purchased) {
                        Button(
                            onClick = { onReturnProductToList() },
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.button_blue)
                            )
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = "Devolver para a lista",
                                    modifier = Modifier.align(Alignment.CenterVertically),
                                    fontSize = 18.sp
                                )
                                Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                            }
                        }
                    } else {
                        Button(
                            onClick = { onAddProductToPurchasedList() },
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.button_green)
                            )
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = "Adicionar à lista de comprados",
                                    modifier = Modifier.align(Alignment.CenterVertically),
                                    fontSize = 18.sp
                                )
                                Icon(imageVector = Icons.Default.Check, contentDescription = null)
                            }
                        }
                    }
                    Button(
                        onClick = onDeleteProductClick,
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red
                        )
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "Deletar produto",
                                modifier = Modifier.align(Alignment.CenterVertically),
                                fontSize = 18.sp
                            )
                            Icon(imageVector = Icons.Default.Close, contentDescription = null)
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun ProductFields(
    title: String,
    content: String
) {
    Column {
        Text(
            text = title,
            modifier = Modifier.fillMaxWidth(),
            fontSize = 14.sp,
            color = Color.Gray
        )
        Text(
            text = content,
            modifier = Modifier.fillMaxWidth(),
            fontSize = 18.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductDetailsScreenPreview() {
    ProductDetailsScreen(
        state = ProductDetailsUiState(
            name = "Sofa",
            description = "Medidas 1,80 x 1,20",
            brand = "Komfort House",
            quantity = "1",
            price = BigDecimal("3000"),
            houseAreaName = "Sala"
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun ProductDetailsScreenPurchasedPreview() {
    ProductDetailsScreen(
        state = ProductDetailsUiState(
            name = "Sofa",
            description = "Medidas 1,80 x 1,20",
            brand = "Komfort House",
            quantity = "1",
            price = BigDecimal("3000"),
            houseAreaName = "Sala",
            purchased = true
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun ProductDetailsScreenIsLoadingPreview() {
    ProductDetailsScreen(
        state = ProductDetailsUiState(
            isLoading = true
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun ProductDetailsScreenWithDeleteErrorPreview() {
    ProductDetailsScreen(
        state = ProductDetailsUiState(
            name = "Sofa",
            description = "Medidas 1,80 x 1,20",
            brand = "Komfort House",
            quantity = "1",
            price = BigDecimal("3000"),
            houseAreaName = "Sala",
            purchased = true,
            deleteErrorMessage = "Falha ao remover produto: Verifique sua conexão de rede"
        )
    )
}