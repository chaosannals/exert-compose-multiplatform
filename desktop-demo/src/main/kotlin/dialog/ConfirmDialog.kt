package dialog

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.onClick
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dp2px
import theme.themeColors
import theme.themeShapes


enum class ConfirmDialogResult {
    None,
    Cancel,
    Yes,
    No,
}

class ConfirmDialogManager: ContentDialogManager() {}

@Composable
fun rememberOpenConfirmDialogForResult(
    title: String="请确认",
    content: String="确定吗？",
    onResult: (ConfirmDialogResult) -> Unit,
) :ConfirmDialogManager {
    return rememberOpenContentDialog(
        factory = { ConfirmDialogManager() },
        onDismissRequest = {
            close()
            onResult(ConfirmDialogResult.Cancel)
        }
    ) {
        ConfirmDialogBox(
            title,
            content,
        ) {
            close()
            onResult(it)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun ConfirmDialogBox(
    title: String,
    content: String,
    onResult: (ConfirmDialogResult) -> Unit,
) {
    val colors by themeColors.collectAsState()
    val shapes by themeShapes.collectAsState()

    Box(
        modifier = Modifier
            .size(300.dp, 200.dp)
            .clip(shapes.dialogBound)
            .background(Color.White)
            .border(1.dp, Color.Black, shapes.dialogBound)
    ) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "close",
            tint = colors.cancelColor,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 10.dp, end = 10.dp)
                .size(20.dp)
                .onClick {
                    onResult(ConfirmDialogResult.Cancel)
                }
        )

        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment= Alignment.Start,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        drawLine(
                            color = Color.Black,
                            start = Offset(0f, size.height),
                            end = Offset(size.width, size.height),
                            strokeWidth = 1f.dp2px,
                        )
                    }
                    .padding(10.dp),
            ) {
                Text(
                    text=title,
                )
            }

            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(10.dp),
            ) {
                SelectionContainer {
                    Text(
                        text=content,
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        drawLine(
                            color = Color.Black,
                            start = Offset(0f, 0f),
                            end = Offset(size.width, 0f),
                            strokeWidth = 1f.dp2px,
                        )
                        drawLine(
                            color = Color.Black,
                            start = Offset(size.width * 0.5f, 0f),
                            end = Offset(size.width * 0.5f, size.height),
                            strokeWidth = 1f.dp2px,
                        )
                    }
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .background(colors.yesColor)
                        .padding(10.dp)
                        .onClick {
                            onResult(ConfirmDialogResult.Yes)
                        }
                ) {
                    Text(
                        text="是",
                        color=Color.White,
                    )
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .background(colors.noColor)
                        .padding(10.dp)
                        .onClick {
                            onResult(ConfirmDialogResult.No)
                        }
                ) {
                    Text(
                        text="否",
                        color=Color.White,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ConfirmDialogPreview() {
    ConfirmDialogBox("请确认", "你确定吗？") {

    }
}
