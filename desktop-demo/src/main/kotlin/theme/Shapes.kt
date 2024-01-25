package theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow

data class ThemeShapes(
    val dialogBound: Shape,
)

val oneShapes = ThemeShapes(
    dialogBound = RoundedCornerShape(10.dp)
)

val themeShapes = MutableStateFlow(oneShapes)
