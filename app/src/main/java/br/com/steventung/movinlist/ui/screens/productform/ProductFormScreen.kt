package br.com.steventung.movinlist.ui.screens.productform

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.steventung.mobilist.R
import coil.compose.AsyncImage

@Composable
fun ProductFormScreen(
    modifier: Modifier = Modifier,
    state: ProductFormUiState,
    onIncreaseQuantity: () -> Unit = {},
    onDecreaseQuantity: () -> Unit = {},
    onExpandDropDownMenu: () -> Unit = {},
    onDismissDropDownMenu: () -> Unit = {},
    onSelectedDropDownMenuItem: (Int) -> Unit = {},
    onClickImage: () -> Unit = {},
    onClickSave: () -> Unit = {}
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
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .windowInsetsPadding(WindowInsets.ime)
        ) {
            AsyncImage(
                model = state.image,
                contentDescription = "imagem produto formulário",
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 250.dp, max = 400.dp)
                    .clickable { onClickImage() },
                contentScale = ContentScale.Fit,
                placeholder = painterResource(R.drawable.imagem_padrao),
                error = painterResource(R.drawable.imagem_padrao),
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = state.name,
                    onValueChange = state.onNameChanged,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Nome Produto") },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        capitalization = KeyboardCapitalization.Words
                    )
                )
                OutlinedTextField(
                    value = state.brand,
                    onValueChange = state.onBrandChanged,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Marca") },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        capitalization = KeyboardCapitalization.Words

                    )
                )
                OutlinedTextField(
                    value = state.description,
                    onValueChange = state.onDescriptionChanged,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 100.dp),
                    label = { Text(text = "Descrição") },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Default,
                        capitalization = KeyboardCapitalization.Sentences
                    )
                )
                OutlinedTextField(
                    value = state.price,
                    onValueChange = state.onPriceChanged,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Preço Unitário") },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done
                    )
                )
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Quantidade: ",
                        fontSize = 18.sp
                    )
                    val cardSize = 36.dp
                    IconButton(
                        onClick = { onDecreaseQuantity() },
                        Modifier
                            .background(color = Color.Gray)
                            .size(cardSize)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowDown,
                            contentDescription = "ícone reduz quantidade"
                        )
                    }
                    Card(
                        Modifier
                            .height(cardSize)
                            .widthIn(min = cardSize),
                        shape = RoundedCornerShape(0)
                    ) {
                        Text(
                            text = state.quantity.toString(),
                            fontSize = 24.sp,
                            modifier = Modifier
                                .align(
                                    Alignment.CenterHorizontally
                                )
                        )
                    }
                    IconButton(
                        onClick = { onIncreaseQuantity() },
                        Modifier
                            .background(color = Color.Gray)
                            .size(cardSize)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowUp,
                            contentDescription = "ícone aumenta quantidade"
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                if (state.isShowDropDownMenu) {
                    if (state.dropDownValue.isEmpty() && state.emptyHouseAreaNameError) {
                        Text(text = "Selecione um cômodo", color = Color.Red)
                    }
                    DropDownMenu(
                        state = state,
                        onDismissDropDownMenu = onDismissDropDownMenu,
                        onExpandDropdownMenu = onExpandDropDownMenu,
                        onSelectedDropDownMenuItem = onSelectedDropDownMenuItem
                    )
                }
                Spacer(Modifier.height(4.dp))
                Button(
                    onClick = { onClickSave() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Salvar")
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownMenu(
    modifier: Modifier = Modifier,
    state: ProductFormUiState,
    onDismissDropDownMenu: () -> Unit = {},
    onExpandDropdownMenu: () -> Unit = {},
    onSelectedDropDownMenuItem: (Int) -> Unit = {}
) {
    ExposedDropdownMenuBox(
        expanded = state.isDropDownExpanded,
        onExpandedChange = { onExpandDropdownMenu() },
        modifier = modifier
    ) {
        TextField(
            value = state.dropDownValue,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = state.isDropDownExpanded)
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            label = { Text(text = "Selecione o cômodo") }
        )
        ExposedDropdownMenu(
            expanded = state.isDropDownExpanded,
            onDismissRequest = {
                onDismissDropDownMenu()
            }
        ) {
            state.houseAreaList.forEachIndexed { index, text ->
                DropdownMenuItem(
                    text = { Text(text = text) },
                    onClick = {
                        onSelectedDropDownMenuItem(index)
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun ProductFormScreenWithOutDropDownMenuPreview() {
    ProductFormScreen(state = ProductFormUiState())
}

@Preview(showBackground = true)
@Composable
private fun ProductFormScreenWithDropDownMenuPreview() {
    ProductFormScreen(
        state = ProductFormUiState(
            isShowDropDownMenu = true,
            emptyHouseAreaNameError = true
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun ProductFormScreenIsLoadingPreview() {
    ProductFormScreen(
        state = ProductFormUiState(
            isLoading = true
        )
    )
}