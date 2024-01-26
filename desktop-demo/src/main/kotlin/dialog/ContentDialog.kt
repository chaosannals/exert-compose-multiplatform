package dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

open class ContentDialogManager {
    private val visibleFlow = MutableStateFlow(false)
    val visible = visibleFlow.asStateFlow()

    fun open() {
        visibleFlow.value = true
    }

    fun close() {
        visibleFlow.value = false
    }
}

@Composable
fun <T: ContentDialogManager> rememberOpenContentDialog(
    factory: () -> T,
    onDismissRequest: T.() -> Unit = { close() },
    content: @Composable T.() -> Unit,
): T {
    val inspectionMode = LocalInspectionMode.current
    val manager = remember {
        factory()
    }

    val visible by manager.visible.collectAsState(inspectionMode)

    if (visible) {
        Dialog(
            onDismissRequest = { manager.onDismissRequest() }
        ) {
            manager.content()
        }
    }

    return manager
}