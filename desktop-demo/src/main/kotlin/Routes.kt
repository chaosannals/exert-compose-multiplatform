import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import kotlinx.coroutines.flow.MutableSharedFlow
import moe.tlaster.precompose.navigation.RouteBuilder
import moe.tlaster.precompose.navigation.transition.NavTransition
import page.EnterPage
import page.IndexPage
import page.LoginPage
import page.aio.FlowDemoPage
import page.db.DbDemoPage
import page.net.JschSshClientPage
import page.net.MinaSshdClientPage
import page.net.MinaSshdServerPage
import page.web.RetrofitDemoPage
import page.web.WebViewPage
import page.awtui.FileDialogPage
import page.usart.SerialComPage

val navigate = MutableSharedFlow<String>()
private val transition = NavTransition(
    createTransition = fadeIn(),
    destroyTransition = fadeOut(),
    pauseTransition = fadeOut(),
    resumeTransition = fadeIn()
)

fun RouteBuilder.buildRoot() {
    scene(
        route = "/enter",
        navTransition = transition,
    ) {
        EnterPage()
    }
    scene(
        route = "/login",
        navTransition = transition,
    ) {
        LoginPage()
    }
    scene(
        route = "/index",
        navTransition = transition,
    ) {
        IndexPage()
    }
}

fun RouteBuilder.buildWeb() {
    scene(
        route="/web/retrofit-demo-page",
        navTransition = transition,
    ) {
        RetrofitDemoPage()
    }
    scene(
        route = "/web/web-view-page",
        navTransition = transition,
    ) {
        WebViewPage()
    }
}

fun RouteBuilder.buildAio() {
    scene(
        route="/aio/flow-demo-page",
        navTransition = transition,
    ) {
        FlowDemoPage()
    }
}

fun RouteBuilder.buildDb() {
    scene(
        route="/db/demo-page",
        navTransition = transition,
    ) {
        DbDemoPage()
    }
}

fun RouteBuilder.buildUi() {
    scene(
        route = "/ui/file-dialog-page",
        navTransition = transition,
    ) {
        FileDialogPage()
    }
}

fun RouteBuilder.buildNet() {
    scene(
        route="/net/jsch-ssh-client-page",
        navTransition = transition,
    ) {
        JschSshClientPage()
    }
    scene(
        route="/net/mina-sshd-client-page",
        navTransition = transition,
    ) {
        MinaSshdClientPage()
    }
    scene(
        route="/net/mina-sshd-server-page",
        navTransition = transition,
    ) {
        MinaSshdServerPage()
    }
}

fun RouteBuilder.buildUsart() {
    scene(
        route="/usart/serial-com-page",
        navTransition = transition,
    ) {
        SerialComPage()
    }
}