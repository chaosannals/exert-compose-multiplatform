import androidx.compose.runtime.*
import dev.datlag.kcef.KCEF
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.math.max


private val kCefDownloadProcessFlow = MutableStateFlow(0)
@OptIn(FlowPreview::class)
val kCefDownloadProcess = kCefDownloadProcessFlow
    .debounce(400)


@Composable
fun ensureKCef(
    onRestartRequired: () -> Unit,
    onError: (Throwable?) -> Unit,
) {
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            KCEF.init(
                builder = {
                    installDir(File("kcef-bundle"))
                    progress {
                        onDownloading {
                            kCefDownloadProcessFlow.value = (max(it, 0f) * 99f).toInt()
                        }
                        onInitialized {
                            kCefDownloadProcessFlow.value = 100
                        }
                    }
                    settings {
                        cachePath = File("cache").absolutePath
                    }
                },
                onError = onError,
                onRestartRequired = onRestartRequired
            )
        }
    }
}