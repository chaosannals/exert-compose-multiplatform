package server

import io.ktor.http.*
import io.ktor.serialization.kotlinx.cbor.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.serialization.kotlinx.protobuf.*
import io.ktor.serialization.kotlinx.xml.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.reflect.*
import kotlinx.html.*
import kotlinx.html.dom.create
import kotlinx.html.dom.document
import kotlinx.html.stream.createHTML
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.json.Json
import kotlinx.serialization.protobuf.ProtoBuf
import nl.adaptivity.xmlutil.XmlDeclMode
import nl.adaptivity.xmlutil.serialization.XML
import server.messages.User

@OptIn(ExperimentalSerializationApi::class)
fun Application.myModule() {
    install(Resources)
    install(ContentNegotiation) {
        // xml 和 json cbor protobuf 互斥。。。。不能单独指定
//        xml(format = XML {
//            xmlDeclMode = XmlDeclMode.Charset
//        })
        json(Json {
            prettyPrint = true
            isLenient = true
        })
//        cbor(Cbor { // ExperimentalSerializationApi
//            ignoreUnknownKeys = true
//        })
//        protobuf(ProtoBuf {
//            encodeDefaults = true
//        })
    }
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

        get("/user/{id}") {
            val uri = call.request.uri
            val id = call.parameters["id"]
            call.respond(
                User(id?.toLong() ?: 0, "uri: $uri", "id：$id")
            )
        }

        get("/users") {
            call.respond(
                message = listOf(
                    User(1, "n1", "a1"),
                    User(2, "n2", "a2"),
                ),
            )
        }
    }

    // 拦截
    intercept(ApplicationCallPipeline.Call) {
        if (call.request.uri == "/aaa") {
            call.respondText("aaa")
        }
    }
}