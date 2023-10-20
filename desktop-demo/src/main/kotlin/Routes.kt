import kotlinx.coroutines.flow.MutableSharedFlow
import moe.tlaster.precompose.navigation.RouteBuilder
import moe.tlaster.precompose.navigation.transition.NavTransition
import page.EnterPage
import page.IndexPage
import page.LoginPage
import page.aio.FlowDemoPage
import page.web.RetrofitDemoPage

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
}

fun RouteBuilder.buildAio() {
    scene(
        route="/aio/flow-demo-page",
        navTransition = NavTransition(),
    ) {
        FlowDemoPage()
    }
}