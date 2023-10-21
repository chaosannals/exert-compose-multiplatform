package page

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import navigate

@Composable
fun IndexButton(
    route: String
) {
    val coroutineScope = rememberCoroutineScope()
    Button(
        onClick = {
            coroutineScope.launch {
                navigate.emit(route)
            }
        }
    ) {
        Text(route)
    }
}

@Composable
@Preview
fun IndexPage() {
    val coroutineScope = rememberCoroutineScope()

    Column {
        IndexButton("/web/retrofit-demo-page")
        IndexButton("/aio/flow-demo-page")
        IndexButton("/db/demo-page")
    }
}