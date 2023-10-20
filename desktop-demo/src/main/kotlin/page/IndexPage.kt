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
@Preview
fun IndexPage() {
    val coroutineScope = rememberCoroutineScope()

    Column {
        Button(
            onClick = {
                coroutineScope.launch {
                    navigate.emit("/web/retrofit-demo-page")
                }
            }
        ) {
            Text("retrofit-demo-page")
        }
    }
}