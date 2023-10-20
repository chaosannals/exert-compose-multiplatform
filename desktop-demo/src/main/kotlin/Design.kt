import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

val density: MutableStateFlow<Density> = MutableStateFlow((Density(1f,1f)))
val widthDp: MutableStateFlow<Dp> = MutableStateFlow(800.dp)
val heightDp: MutableStateFlow<Dp> = MutableStateFlow(600.dp)

// 目前没找到直接获取 density 的方法，只能通过 Compose 阶段的 LocalDensity 回传
@Composable
fun PickDensity() {
    val ld = LocalDensity.current

    LaunchedEffect(ld) {
        density.value = ld
    }
}

//val displayWidth: Int by lazy {
//    Resources.getSystem().displayMetrics.widthPixels
//}
//
//val displayHeight: Int by lazy {
//    Resources.getSystem().displayMetrics.heightPixels
//}
//
//val displayDp: Dp by lazy {
//    floor(displayWidth / density).dp
//}
//
//val designInt : Int = 375
//val designDp: Dp = designInt.dp
//val ratio: Float = displayDp / designDp