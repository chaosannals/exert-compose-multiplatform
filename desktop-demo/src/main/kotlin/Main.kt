import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import kotlinx.coroutines.flow.onSubscription
import moe.tlaster.precompose.PreComposeWindow
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.navigation.transition.NavTransition

@Composable
@Preview
fun App() {
    MaterialTheme {
        val navigator = rememberNavigator()

        LaunchedEffect(Unit) {
            navigate.collect {
                navigator.navigate(it)
            }
        }

        NavHost(
            navigator = navigator,
            navTransition = NavTransition(),
            initialRoute = "/enter"
        ) {
            buildRoot()
            buildWeb()
        }
    }
}

fun main() = application {
    PickDensity()
    val width by widthDp.collectAsState()
    val height by heightDp.collectAsState()

//    Window(
    PreComposeWindow(
        onCloseRequest = ::exitApplication,
        title = "Compose Multiplatform for Desktop Demo.",
        state = WindowState(
            width = width,
            height = height,
        ),
        resizable = false,
    ) {
        App()
    }
}
