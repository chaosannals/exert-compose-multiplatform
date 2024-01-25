package ui.layout

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun PageLayout(
    modifier: Modifier=Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    val layoutModifier by remember(modifier) {
        derivedStateOf {
            modifier.fillMaxSize()
        }
    }

    Box(layoutModifier) {
        content()
    }
}

@Preview
@Composable
fun PageLayoutPreview() {
    PageLayout(
        modifier = Modifier
            .background(Color.Red)
    ) {

    }
}