package page.net

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Jsch 很久没有维护
 */
@Composable
@Preview
fun JschSshClientPage() {
    var host by remember {
        mutableStateOf("")
    }
    var port by remember {
        mutableStateOf("22")
    }
    var user by remember {
        mutableStateOf("root")
    }
    var privateKey by remember {
        mutableStateOf("")
    }

    var isShowPrivateKey by remember {
        mutableStateOf(false)
    }

    FilePicker(
        show = isShowPrivateKey,
        // fileExtensions = listOf("pem", "ppk"),
    ) {
        isShowPrivateKey = false
        it?.run {
            val p = Paths.get(path)
            val b = Files.readAllBytes(p)
            privateKey = b
                .decodeToString()
            System.out.println(privateKey)
        }
    }

    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Text(privateKey)
        Button(
            onClick = {
                isShowPrivateKey = true
            }
        ) {
            Text("加载密钥")
        }
    }
}