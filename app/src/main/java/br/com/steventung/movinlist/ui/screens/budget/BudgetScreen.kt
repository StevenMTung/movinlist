package br.com.steventung.movinlist.ui.screens.budget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
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
import br.com.steventung.movinlist.extensions.toBrazilianCurrency
import java.math.BigDecimal

@Composable
fun BudgetScreen(
    modifier: Modifier = Modifier,
    state: BudgetUiState,
    onExpandPendingBudgetSection: () -> Unit = {},
    onExpandPurchasedBudgetSection: () -> Unit = {},
    onExpandTotalExpensesBudgetSection: () -> Unit = {}
) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp)
                .verticalScroll(
                    rememberScrollState()
                ),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "Relatório Financeiro",
                    fontSize = 18.sp,
                    modifier = Modifier.fillMaxWidth(),
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                )
            }
            BudgetSection(
                backgroundColor = R.color.purchased_background,
                budgetSection = state.pendingProductsSection,
                budget = state.pendingBudget,
                expandedState = state.pendingIsExpanded,
                budgetSectionTitle = "Orçamento Lista Pendentes",
                onExpandBudgetSection = onExpandPendingBudgetSection
            )
            BudgetSection(
                backgroundColor = R.color.purchased_section_title,
                budgetSection = state.purchasedProductsSection,
                budget = state.purchasedBudget,
                expandedState = state.purchasedIsExpanded,
                budgetSectionTitle = "Orçamento Lista Comprados",
                onExpandBudgetSection = onExpandPurchasedBudgetSection
            )
            BudgetSection(
                backgroundColor = R.color.teal_600,
                budgetSection = state.totalExpensesProductsSection,
                budget = state.totalExpensesBudget,
                expandedState = state.totalExpensesIsExpanded,
                budgetSectionTitle = "Orçamento Total",
                onExpandBudgetSection = onExpandTotalExpensesBudgetSection
            )
        }
}

@Composable
private fun BudgetSection(
    backgroundColor: Int,
    budgetSection: Map<String, BigDecimal>,
    budget: BigDecimal,
    expandedState: Boolean,
    budgetSectionTitle: String,
    modifier: Modifier = Modifier,
    onExpandBudgetSection: () -> Unit = {}
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = colorResource(backgroundColor))
                .clickable { onExpandBudgetSection() }
        ) {
            Text(
                text = budgetSectionTitle + ": " +
                        budget.toBrazilianCurrency(),
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f)
                    .align(Alignment.CenterVertically),
                fontSize = 16.sp
            )
            if (expandedState) {
                Icon(
                    Icons.Default.KeyboardArrowUp,
                    contentDescription = "ícone retração",
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            } else {
                Icon(
                    Icons.Default.KeyboardArrowDown,
                    contentDescription = "ícone expansão",
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }
        AnimatedVisibility(visible = expandedState) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(
                        start = 30.dp,
                        end = 6.dp,
                        top = 6.dp,
                        bottom = 16.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    budgetSection.forEach { section ->
                        val houseAreaName = section.key
                        val pendingBudget = section.value
                        Text(
                            text = "$houseAreaName: ${pendingBudget.toBrazilianCurrency()}",
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BudgetScreenWithdrawnPreview() {
    BudgetScreen(
        state = BudgetUiState(
            pendingBudget = BigDecimal("10000"),
            purchasedBudget = BigDecimal("14000"),
            totalExpensesBudget = BigDecimal("24000")
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun BudgetScreenExpandedPreview() {
    BudgetScreen(
        state = BudgetUiState(
            pendingBudget = BigDecimal("10000"),
            purchasedBudget = BigDecimal("14000"),
            totalExpensesBudget = BigDecimal("24000"),
            pendingProductsSection = mapOf(
                "Sala" to BigDecimal("2000"),
                "Cozinha" to BigDecimal("3500"),
                "Quarto" to BigDecimal("4500")
            ),
            purchasedProductsSection = mapOf(
                "Sala" to BigDecimal("6000"),
                "Cozinha" to BigDecimal("3500"),
                "Quarto" to BigDecimal("4500")
            ),
            totalExpensesProductsSection = mapOf(
                "Sala" to BigDecimal("8000"),
                "Cozinha" to BigDecimal("7000"),
                "Quarto" to BigDecimal("9000")
            ),
            pendingIsExpanded = true,
            purchasedIsExpanded = true,
            totalExpensesIsExpanded = true

        )
    )
}