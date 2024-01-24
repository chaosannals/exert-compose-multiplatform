package page.web

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewState
import dp2px
import kCefDownloadProcess
import kCefInitialized
import kCefInitializedAt

@Composable
fun WebViewPage() {
    val state = rememberWebViewState("https://baidu.com")

    val kCefProcess by kCefDownloadProcess.collectAsState(0)
    val kCefAt by kCefInitializedAt.collectAsState(0)
    val kCef by kCefInitialized.collectAsState(false)

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .drawBehind {
                    drawLine(
                        color = Color.Blue,
                        start = Offset(0f, 1f.dp2px),
                        end = Offset((kCefProcess.toFloat() / 100) * size.width, 1f.dp2px),
                        strokeWidth = 2f.dp2px,
                    )
                },
        )
        if (kCef) {
            key(kCefAt) {
                WebView(
                    state = state,
                    modifier = Modifier
                        .fillMaxSize()
                        .border(2.dp, Color.Black)
                )
            }
        }
    }
}