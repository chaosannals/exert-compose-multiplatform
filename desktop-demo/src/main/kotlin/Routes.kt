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

fun RouteBuilder.buildRoot() {
    scene(
        route = "/enter",
        navTransition = NavTransition(),
    ) {
        EnterPage()
    }
    scene(
        route = "/login",
        navTransition = NavTransition(),
    ) {
        LoginPage()
    }
    scene(
        route = "/index",
        navTransition = NavTransition(),
    ) {
        IndexPage()
    }
}

fun RouteBuilder.buildWeb() {
    scene(
        route="/web/retrofit-demo-page",
        navTransition = NavTransition(),
    ) {
        RetrofitDemoPage()
    }
    scene(
        route = "/web/web-view-page",
        navTransition = NavTransition(),
    ) {
        WebViewPage()
    }
}

fun RouteBuilder.buildAio() {
    scene(
        route="/aio/flow-demo-page",
        navTransition = NavTransition(),
    ) {
        FlowDemoPage()
    }
}

fun RouteBuilder.buildDb() {
    scene(
        route="/db/demo-page",
        navTransition = NavTransition(),
    ) {
        DbDemoPage()
    }
}

fun RouteBuilder.buildUi() {
    scene(
        route = "/ui/file-dialog-page",
        navTransition = NavTransition(),
    ) {
        FileDialogPage()
    }
}

fun RouteBuilder.buildNet() {
    scene(
        route="/net/jsch-ssh-client-page",
        navTransition = NavTransition(),
    ) {
        JschSshClientPage()
    }
    scene(
        route="/net/mina-sshd-client-page",
        navTransition = NavTransition(),
    ) {
        MinaSshdClientPage()
    }
    scene(
        route="/net/mina-sshd-server-page",
        navTransition = NavTransition(),
    ) {
        MinaSshdServerPage()
    }
}

fun RouteBuilder.buildUsart() {
    scene("/usart/serial-com-page") {
        SerialComPage()
    }
}