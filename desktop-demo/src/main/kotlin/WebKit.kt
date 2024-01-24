import androidx.compose.runtime.*
import dev.datlag.kcef.KCEF
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.math.max


private val kCefDownloadProcessFlow = MutableStateFlow(0)
private val kCefInitializedAtFlow = MutableStateFlow(0L)
private val kCefInitializedFlow = MutableStateFlow(false)

@OptIn(FlowPreview::class)
val kCefDownloadProcess = kCefDownloadProcessFlow.debounce(400)
val kCefInitializedAt = kCefInitializedAtFlow.asStateFlow()
val kCefInitialized = kCefInitializedFlow.asStateFlow()

@Composable
fun ensureKCef() {
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            KCEF.init(
                builder = {
                    installDir(File("kcef-bundle"))
                    progress {
                        onDownloading {
                            kCefDownloadProcessFlow.value = max(it, 0f).toInt()
                            println("download: ${kCefDownloadProcessFlow.value}")
                        }
                        onInitialized {
                            kCefDownloadProcessFlow.value = 100
                            kCefInitializedAtFlow.value = System.currentTimeMillis()
                            kCefInitializedFlow.value = true
                        }
                    }
                    settings {
                        cachePath = File("cache").absolutePath
                    }
                },
                onError = {
                    it?.let {
                        ioScope.launch {
                            eventExceptionFlow.emit(it)
                        }
                    }
                },
                onRestartRequired = {
                    kCefInitializedAtFlow.value = System.currentTimeMillis()
                }
            )
        }
    }
}