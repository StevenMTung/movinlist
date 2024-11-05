package br.com.steventung.movinlist.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.steventung.mobilist.R


sealed class BottomAppBarItem(
    val icon: Int,
    val label: String
) {
    data object Home : BottomAppBarItem(
        icon = R.drawable.ic_action_home,
        label = "Home"
    )

    data object ProductList : BottomAppBarItem(
        icon = R.drawable.ic_action_list,
        label = "Pendentes"
    )

    data object Puchased : BottomAppBarItem(
        icon = R.drawable.ic_action_check,
        label = "Comprados"
    )

    data object Budget : BottomAppBarItem(
        icon = R.drawable.ic_action_budget,
        label = "Orçamento"
    )
}

val bottomAppBarItemList = listOf(
    BottomAppBarItem.Home,
    BottomAppBarItem.ProductList,
    BottomAppBarItem.Puchased,
    BottomAppBarItem.Budget,
)

@Composable
fun MovinListBottomAppBar(
    modifier: Modifier = Modifier,
    item: BottomAppBarItem,
    items: List<BottomAppBarItem> = emptyList(),
    onItemChanged: (BottomAppBarItem) -> Unit = {}
) {
    NavigationBar(modifier = modifier) {
        items.forEach {
            val icon = it.icon
            val label = it.label
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(icon),
                        contentDescription = label
                    )
                },
                label = { Text(text = label) },
                selected = item.label == label,
                onClick = { onItemChanged(it) }
            )
        }
    }
}

@Preview
@Composable
private fun MobiListBottomAppBarPreview() {
    MovinListBottomAppBar(items = bottomAppBarItemList, item = BottomAppBarItem.Home)
}