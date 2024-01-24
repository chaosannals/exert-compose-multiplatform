package page.awtui

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.window.AwtWindow
import java.awt.FileDialog
import java.awt.Frame

// 官方的这个封装方案有点问题，还是建议使用第三方库 的 com.darkrockstudios.libraries.mpfilepicker.FilePicker
@Composable
fun FileDialogPage(
    parent: Frame? = null,
) {
    var isOpen by remember { mutableStateOf(false) }

    Button(
        onClick = {
            isOpen = !isOpen
        }
    ) {
        Text("显示")
    }

    if (isOpen) {
        AwtWindow(
            create = {
                object :FileDialog(parent, "选择文件", LOAD) {
                    // 虽然名字叫 setVisible ，但是关闭窗口的时候这个事件会被调用2次。
                    // 一次 false 紧接一次 true
                    override fun setVisible(b: kotlin.Boolean) {
                        super.setVisible(b)
                        if (b) { // 由于这个奇怪的操作，官方示例在这里只给 true 应用
                            // 这样操作就使得窗口为 true 时被设置成 false 关闭。
                            isOpen = false
                        }
                    }
                }
            },
            dispose = FileDialog::dispose
        )
    }
}