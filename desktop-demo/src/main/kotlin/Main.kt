import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.launch
import moe.tlaster.precompose.PreComposeWindow
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.navigation.transition.NavTransition
import org.jetbrains.exposed.sql.Database
import server.myModule

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
            buildAio()
            buildDb()
            buildNet()
        }
    }
}

fun main() = application {
    val coroutineScope = rememberCoroutineScope()

    coroutineScope.launch {
        database.emit(Database.connect(
//            "jdbc:h2:mem:test",
            "jdbc:h2:file:~/test.db", // 不同平台路径不好确定，没有专门的路径管理系统。用户空间路径 ~ 是统一的。
            driver = "org.h2.Driver",
            user = "root",
            password = ""
        ))
    }

    PickDensity()
    val width by widthDp.collectAsState()
    val height by heightDp.collectAsState()

    // 启动内嵌的服务器
    embeddedServer(
        Netty, // 支持 Netty, Jetty, Tomcat , CIO ,要装各自的包，示例只装了 Netty 包，详见 build.gradle.kts
        port= 43210,
        watchPaths = listOf("MainKt"),
        module = Application::myModule,
    ).start(wait = false)

    val state = rememberWindowState(
        width = width,
        height = height,
    )

    //    Window(
    PreComposeWindow(
        onCloseRequest = ::exitApplication,
        title = "Compose Multiplatform for Desktop Demo.",
        state = state,
        resizable = true,
    ) {
        App()
    }
}
