package com.badrqaba.core_ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.badrqaba.core_ui.theme.PocketGoodsTheme

@Composable
fun <T> GridLayout(
    columns: Int,
    items: List<T>,
    modifier: Modifier = Modifier,
    content: @Composable (item: T, index: Int) -> Unit
) {
    Column(modifier = modifier) {
        var rows = (items.size / columns)
        if (items.size.mod(columns) > 0) {
            rows += 1
        }

        for (rowId in 0 until rows) {
            val firstIndex = rowId * columns

            Row {
                for (columnId in 0 until columns) {
                    val index = firstIndex + columnId
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        if (index < items.size) {
                            content(items[index], index)
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun GridLayoutPreview() {
    PocketGoodsTheme {
        val items = (0..10).toList()

        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Products",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(all = 15.dp)
            )
            GridLayout(
                columns = 2,
                items = items,
                modifier = Modifier.padding(
                    horizontal =8.dp
                )
            ) { item, _ ->
                Card(modifier = Modifier.padding(all = 8.dp)) {
                    Text(
                        text = item.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(all = 10.dp)
                    )
                }
            }
        }
    }
}