package page.web

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewState
import kCefDownloadProcess

@Composable
fun WebViewPage() {
    val state = rememberWebViewState("https://baidu.com")

    val kCef by kCefDownloadProcess.collectAsState(0)

    key(kCef) {
        WebView(
            state = state,
            modifier = Modifier
                .fillMaxSize()
                .border(2.dp, Color.Black)
        )
    }
}