package theme

import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.MutableStateFlow

data class ThemeColors(
    val yesColor: Color,
    val noColor: Color,
    val cancelColor: Color,
)

val oneColors = ThemeColors(
    yesColor = Color(0xFF4499FF),
    noColor = Color(0xFFFF4444),
    cancelColor = Color(0xFF999999),
)

val themeColors = MutableStateFlow(oneColors)
