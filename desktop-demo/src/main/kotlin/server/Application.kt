package server

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.*
import kotlinx.html.dom.create
import kotlinx.html.dom.document
import kotlinx.html.stream.createHTML
import kotlinx.serialization.json.Json
import server.messages.User

val json = Json {

}

fun Application.myModule() {
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Patch)
        allowMethod(HttpMethod.Delete)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.XForwardedProto)
        allowHeadersPrefixed("custom-") // 允许的前缀
        exposeHeader("aliyun-x") // 排除的
        // allowHost("localhost", subDomains= listOf("www", "m"), schemes = listOf("http", "https"))
        anyHost()
        allowCredentials = true
        allowNonSimpleContentTypes = true
        maxAgeInSeconds = 24 * 60 * 60
    }
    routing {
        get("/") {
            call.response.header("Content-Type", "text/html")
            call.respondText(
                createHTML().html {
                    head {
                        meta(charset = "utf-8")
                        title("Desktop Backend")
                        style {
                            unsafe {
                                raw("""
                                    .item-list > span {
                                        display: block;
                                        color: red;
                                    }
                                """.trimIndent())
                            }
                        }
                    }
                    body {
                        h1 { +"Hello! Index Demo" }
                        p { + "a compose multiplatform desktop backend server demo."}
                        div(classes="item-list") {
                            span { +"1" }
                            span { +"2" }
                        }

                        script(type = ScriptType.textJScript) {
                            unsafe {
                                raw("alert('提示');")
                            }
                        }
                    }
                }
            )
        }
        post("/users") {
            call.response.header("Content-Type", "application/json")
            call.respond(
                listOf(
                    User(1, "n1", "a1"),
                    User(2, "n2", "a2"),
                )
            )
        }
    }
}