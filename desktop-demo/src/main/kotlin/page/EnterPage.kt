package page

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay
import navigate

@Composable
@Preview
fun EnterPage() {
    LaunchedEffect(Unit) {
        delay(1000)
        navigate.emit("/index")
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text("enter")
    }
}