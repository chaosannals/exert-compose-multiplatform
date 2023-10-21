package page.web

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import server.messages.User

interface LocalhostService {
    @GET("/")
    suspend fun index() : ResponseBody

    @GET("/user/{id}")
    suspend fun user(@Path("id") id: Long): User

    @GET("/users")
    suspend fun users() : List<User>
}

@Composable
@Preview
fun RetrofitDemoPage() {
    val localhost = remember {
        Retrofit.Builder()
            .baseUrl("http://127.0.0.1:43210")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val localhostService = remember(localhost) {
        localhost.create(LocalhostService::class.java)
    }

    var text by remember {
        mutableStateOf("")
    }

    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxSize()
    ) {
        Button(
            onClick = {
                coroutineScope.launch {
                    text = localhostService
                        .index()
                        .string()
                }
            }
        ) {
            Text("请求 index")
        }
        Button(
            onClick = {
                coroutineScope.launch {
                    text = localhostService
                        .user(1).toString()
                }
            }
        ) {
            Text("请求 user")
        }

        Button(
            onClick = {
                coroutineScope.launch {
                    text = localhostService
                        .users().toString()
                }
            }
        ) {
            Text("请求 users")
        }
        Text(text)
    }
}