package page.usart

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.onClick
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import com.fazecast.jSerialComm.SerialPort
import io.ktor.network.selector.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.h2.table.Column

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SerialComPage() {
    val coroutineScope = rememberCoroutineScope()

    var expanded by remember { mutableStateOf(false) }

    val lines = remember {
        mutableStateListOf<String>()
    }

    var comBaudRate by remember {
        mutableIntStateOf(115200)
    }
    val comBaudRates = remember {
        mutableStateListOf(
            1200,2400,4800,9600,19200,38400,43000,56000,57600,115200
        )
    }
    var comPort: SerialPort? by remember {
        mutableStateOf(null)
    }
    val comPorts = remember {
        mutableStateListOf<SerialPort>()
    }

    LaunchedEffect(Unit) {
        comPorts.clear()
        comPorts.addAll(SerialPort.getCommPorts())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row {
            Button(
                onClick = { expanded = !expanded }
            ) {
                Text("${comPort?.systemPortName}")
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    comPorts.forEach {
                        DropdownMenuItem(
                            onClick = {
                                comPort = it
                                expanded = false
                            }
                        ) {
                            Text(it.systemPortName)
                        }
                    }
                }
            }

            Button(
                onClick = {
                    coroutineScope.launch(Dispatchers.IO) {
                        comPort?.run {
                            try {
                                baudRate = 115200
                                openPort()
                                while(true) {
                                    while (bytesAvailable() == 0) {
                                        delay(20)
                                    }
                                    val buffer = ByteArray(bytesAvailable())
                                    val readCount = readBytes(buffer, buffer.size)
                                    val text = buffer
                                        .slice(0 until readCount)
                                        .toByteArray()
                                        .decodeToString()
                                    lines.add("read count: $readCount | $text")
                                }
                            } catch (e: Exception) {
                                lines.add(e.message ?: "error")
                            } finally {
                                closePort()
                            }
                        }
                    }
                },
                enabled = comPort != null,
            ) {
                Text("打开")
            }

            var baudExpanded by remember { mutableStateOf(false) }
            Column {
                TextField(
                    value = comBaudRate.toString(),
                    onValueChange = {
                        it.toIntOrNull()?.let { i ->
                            comBaudRate = i
                        }
                    },
                    modifier = Modifier
                        .onClick {
                            baudExpanded = true
                        }
                        .onFocusChanged {
                            baudExpanded = it.hasFocus
                        },
                )
                DropdownMenu(
                    expanded = baudExpanded,
                    onDismissRequest = { baudExpanded = false},
                    scrollState = rememberScrollState(),
                    modifier = Modifier.heightIn(100.dp, 300.dp)
                ) {
                    comBaudRates.forEach {
                        DropdownMenuItem(
                            onClick = {
                                comBaudRate = it
                                baudExpanded = false
                            }
                        ) {
                            Text(it.toString())
                        }
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            lines.forEach {
                SelectionContainer {
                    Text(it)
                }
            }
        }
    }
}

