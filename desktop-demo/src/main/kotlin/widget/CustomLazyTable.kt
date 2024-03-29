package widget

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

// TODO

class CustomLazyTableColumnScope<T>() {
    var headCompose: @Composable ColumnScope.() -> Unit = {}
    var cellCompose: @Composable BoxScope.(T) -> Unit = { Text(it.toString())}

    fun head(content: @Composable ColumnScope.() -> Unit) {
        headCompose = content
    }

    fun cell(content: @Composable BoxScope.(T) -> Unit) {
        cellCompose = content
    }
}

class CustomLazyTableScope<T>() {
    val columnsCompose = mutableListOf<CustomLazyTableColumnScope<T>>()

    fun column(definition: CustomLazyTableColumnScope<T>.() -> Unit) {
        val columnScope = CustomLazyTableColumnScope<T>().apply {
            definition()
        }
        columnsCompose.add(columnScope)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> CustomLazyTable(
    items: List<T>,
    definition: CustomLazyTableScope<T>.() -> Unit
) {
    val scope = remember(definition) {
        CustomLazyTableScope<T>().apply {
            definition()
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row {
            scope.columnsCompose.forEach {
                Column(
                    modifier = Modifier
                ) {
                    it.headCompose.invoke(this)
                }
            }
        }
        LazyColumn {
            itemsIndexed(items) { _, cell ->
                Row {
                    scope.columnsCompose.forEach {
                        Box(
                            modifier = Modifier
                        ) {
                            it.cellCompose.invoke(this, cell)
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun CustomLazyTablePreview() {
    val texts = remember {
        mutableStateListOf(
            "aaa",
            "bbb",
            "ccc",
            "aaa",
            "bbb",
            "ccc",
            "aaa",
            "bbb",
            "ccc",
            "aaa",
            "bbb",
            "ccc",
            "aaa",
            "bbb",
            "ccc",
            "aaa",
            "bbb",
            "ccc",
        )
    }

    CustomLazyTable(texts) {
        column {
            head {
                Text("头部1")
            }
        }
        column {
            head {
                var text2 by remember {
                    mutableStateOf("aaa")
                }
                Text("头部2")
                TextField(
                    value = text2,
                    onValueChange = { text2 = it }
                )
            }
        }
        column {
            head {
                Text("头部3")
            }
            cell {
                Text("内容3: $it")
            }
        }
    }
}
