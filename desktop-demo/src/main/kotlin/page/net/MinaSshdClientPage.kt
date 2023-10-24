package page.net

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.apache.sshd.client.SshClient
import org.apache.sshd.client.channel.ClientChannelEvent
import util.keyPair
import util.readPrivateKey
import java.io.ByteArrayOutputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

/**
 * 是一个 Apache 项目，Mina 是个多功能的网路库
 * 子项目 Mina-Sshd 虽然名字 Sshd 看着像个服务端项目，实际上有 ssh 客户端支持
 */

val minaSshdClient = MutableStateFlow<SshClient>(SshClient.setUpDefaultClient())

@Composable
@Preview
fun MinaSshdClientPage() {
    val client by minaSshdClient.collectAsState()
    var host by remember {
        mutableStateOf("127.0.0.1")
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
    var privateKeyBytes by remember {
        mutableStateOf(byteArrayOf())
    }
    var privateKeyPath by remember {
        mutableStateOf("")
    }
    var isShowPrivateKey by remember {
        mutableStateOf(false)
    }

    FilePicker(
        show = isShowPrivateKey,
        // fileExtensions = listOf("pem", "ppk", "key"),
    ) {
        isShowPrivateKey = false
        it?.run {
            privateKeyPath = path
            val p = Paths.get(path)
            privateKeyBytes = Files.readAllBytes(p)
            privateKey = privateKeyBytes
                .decodeToString()
            System.out.println(privateKey)
        }
    }

    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        client.start()
    }

    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        TextField(
            host,
            label = { Text("主机")},
            onValueChange = {host = it},
        )
        TextField(
            port,
            label = { Text("端口")},
            onValueChange = {port = it},
        )
        TextField(
            user,
            label = { Text("用户")},
            onValueChange = {user = it},
        )
        Text(privateKeyPath)
        Text(privateKey)
        Button(
            onClick = {
                isShowPrivateKey = true
            }
        ) {
            Text("加载密钥")
        }
        Button(
            onClick = {
                coroutineScope.launch {
                    try {
                        client.connect(user, host, port.toInt()).verify(4000).session.use {
                           val priKey = readPrivateKey(privateKey)
                           it.addPublicKeyIdentity(priKey.keyPair)
                            // it.addPasswordIdentity("123456")
                            it.auth().verify(4000)
                            System.out.println("session auth")
                            it.createExecChannel("pwd").use {
                                ByteArrayOutputStream().use { stream ->
                                    it.out = stream
                                    it.err = stream
                                    it.open().verify(10000)
                                    System.out.println("channel auth")
                                    it.waitFor(EnumSet.of(ClientChannelEvent.CLOSED), 10000L)
                                    privateKey = stream.toString()
                                }
                            }
                        }
                    } catch (t: Throwable) {
                        System.out.println(t.message)
                    }
                }
            }
        ) {
            Text("链接")
        }
    }
}