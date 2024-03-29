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
        IndexButton("/web/web-view-page")
        IndexButton("/aio/flow-demo-page")
        IndexButton("/db/demo-page")
        IndexButton("/net/jsch-ssh-client-page")
        IndexButton("/net/mina-sshd-client-page")
        IndexButton("/net/mina-sshd-server-page")
        IndexButton("/ui/file-dialog-page")
        IndexButton("/usart/serial-com-page")

        IndexButton("/cmui/confirm-dialog-page")
        IndexButton("/cmui/lazy-layout-page")
    }
}