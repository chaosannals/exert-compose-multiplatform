package page.cmui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import dialog.ConfirmDialogResult
import dialog.rememberOpenConfirmDialogForResult

@Composable
fun ConfirmDialogPage() {
    var result by remember {
        mutableStateOf(ConfirmDialogResult.None)
    }

    val manager = rememberOpenConfirmDialogForResult {
        result = it
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Button(
            onClick = {
                manager.open()
            }
        ) {
            Text("操作")
        }
        Text("结果：${result.name}")
    }
}