package page.aio

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

val p1Flow = MutableSharedFlow<Int>(0)
val p2Flow = MutableSharedFlow<Int>(0)

@Composable
@Preview
fun FlowDemoPage() {
    val coroutineScope = rememberCoroutineScope()

    var p1 by remember {
        mutableStateOf(0)
    }

    var p2 by remember {
        mutableStateOf(0)
    }

    LaunchedEffect(Unit) {
        // 挂起后会通过 launched 的机制，所以不需要和 Rx 那样使用 Dispoable
        p1Flow.collect {
            p1 = it
        }

        // 此协程会被 p1 挂住，导致 p2 的挂起没有执行。
        // 所以每个 LaunchedEffect 只能有一个 collect 被调用，这里需要另起一个。
        p2Flow.collect {
            p2 = it
        }
    }

    Column {
        Button(
            onClick = {
                coroutineScope.launch {
                    p1Flow.emit(p1 + 2)
                }
            }
        ) {
            Text("p1 = ${p1}")
        }

        Button(
            onClick = {
                coroutineScope.launch {
                    p2Flow.emit(p2 + 2)
                }
            }
        ) {
            Text("p2 = ${p2}")
        }
    }
}