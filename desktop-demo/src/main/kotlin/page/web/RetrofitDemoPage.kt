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
import retrofit2.http.GET

interface BaiduService {
    @GET("/")
    fun index() : Call<ResponseBody>
}

@Composable
@Preview
fun RetrofitDemoPage() {
    val baidu = remember {
        Retrofit.Builder()
            .baseUrl("https://baidu.com")
            .build()
    }
    val baiduService = remember(baidu) {
        baidu.create(BaiduService::class.java)
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
                    text = baiduService.index().execute().body()?.string() ?: "null"
                }
            }
        ) {
            Text("请求 index")
        }
        Text(text)
    }
}