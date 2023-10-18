package page

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity

@Composable
@Preview
fun LoginPage() {
    val density = LocalDensity.current

    Column(
        modifier = Modifier
    ) {
        Text("${density}")
    }
}