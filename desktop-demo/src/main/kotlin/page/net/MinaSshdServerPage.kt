package page.net

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.MutableStateFlow
import org.apache.sshd.client.SshClient
import org.apache.sshd.common.file.virtualfs.VirtualFileSystemFactory
import org.apache.sshd.common.session.SessionContext
import org.apache.sshd.scp.server.ScpCommandFactory
import org.apache.sshd.server.SshServer
import org.apache.sshd.server.auth.keyboard.KeyboardInteractiveAuthenticator
import org.apache.sshd.server.auth.password.PasswordAuthenticator
import org.apache.sshd.server.auth.pubkey.PublickeyAuthenticator
import org.apache.sshd.server.channel.ChannelSession
import org.apache.sshd.server.command.Command
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider
import org.apache.sshd.server.shell.InteractiveProcessShellFactory
import org.apache.sshd.server.shell.ProcessShellFactory
import org.apache.sshd.server.shell.ShellFactory
import org.apache.sshd.sftp.server.SftpSubsystemFactory
import java.nio.file.Path
import java.nio.file.Paths

/**
 * 是一个 Apache 项目，Mina 是个多功能的网路库
 * 子项目 Mina-Sshd 提供 服务端 和 客户端
 * 此服务的协议方面问题不大，但是好像 windows 下的命令兼容有问题。
 * TODO linux 试试
 */

val minaSshdServer = MutableStateFlow<SshServer>(SshServer.setUpDefaultServer())

@Composable
@Preview
fun MinaSshdServerPage() {
    val server by minaSshdServer.collectAsState()

    var host by remember {
        mutableStateOf("0.0.0.0")
    }
    var port by remember {
        mutableStateOf("22")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Button(
            onClick = {
                val homeDir = Paths.get(System.getProperty("user.home")).toAbsolutePath()
                val keyPath = Paths.get(homeDir.toString(), ".ssh", "id_rsa")
                System.out.println("keyPath: $keyPath")
                server.host = host
                server.port = port.toInt()
                server.keyPairProvider = SimpleGeneratorHostKeyProvider(keyPath)
                server.passwordAuthenticator = PasswordAuthenticator { username, password, session ->
                    // TUDO 做验证 排除 root
                    System.out.println("passwordAuth: $username")
                    true
                }
                server.publickeyAuthenticator = PublickeyAuthenticator { username, key, session ->
                    // TUDO 做验证
                    System.out.println("publicKeyAuth: $username")
                    true
                }

                // scp
                server.commandFactory = ScpCommandFactory()

                // sftp 子系统
                server.subsystemFactories = listOf(SftpSubsystemFactory())
                // sftp 默认目录 /
                System.out.println("sftp home: $homeDir")
                server.fileSystemFactory = object: VirtualFileSystemFactory(homeDir) {
                    override fun getUserHomeDir(session: SessionContext?): Path {
                        val hd = super.getUserHomeDir(session)
                        System.out.println("getUserHomeDir: $hd")
                        return hd
                    }
                }
                // shell 环境
//                server.shellFactory = InteractiveProcessShellFactory.INSTANCE
                server.shellFactory = ShellFactory { channel ->
                    val s = channel.session
                    val user = s.username
                    System.out.println("ShellFactory: $user")
                    InteractiveProcessShellFactory.INSTANCE.createShell(channel)
                }
                server.start()
                System.out.println("sshd start")
            }
        ) {
            Text("启动服务")
        }
    }
}