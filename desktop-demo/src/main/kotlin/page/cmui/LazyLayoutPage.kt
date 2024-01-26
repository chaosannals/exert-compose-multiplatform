package page.cmui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.layout.LazyLayout
import androidx.compose.foundation.lazy.layout.LazyLayoutItemProvider
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.random.Random

private data class CustomLayoutItem(
    val x: Int,
    val y: Int,
)

private class CustomLayoutBoundaries(
    val fromX: Int,
    val toX: Int,
    val fromY: Int,
    val toY: Int,
)

private typealias CustomLayoutComposableItemContent = @Composable (CustomLayoutItem) -> Unit

private data class CustomLayoutItemContent(
    val item: CustomLayoutItem,
    val itemContent: CustomLayoutComposableItemContent,
)

@Composable
private fun rememberCustomLayoutState(): CustomLayoutState {
    return remember { CustomLayoutState() }
}

@Stable
private class CustomLayoutState {
    private val _offsetState = mutableStateOf(IntOffset.Zero)
    val offsetState get() = _offsetState

    fun onDrag(offset: IntOffset) {
        val x = (_offsetState.value.x - offset.x).coerceAtLeast(0)
        val y = (_offsetState.value.y - offset.y).coerceAtLeast(0)
        _offsetState.value = IntOffset(x, y)
    }

    fun getBoundaries(
        constraints: Constraints,
        threshold: Int = 500,
    ): CustomLayoutBoundaries {
        return CustomLayoutBoundaries(
            fromX = offsetState.value.x - threshold,
            toX = constraints.maxWidth + offsetState.value.x + threshold,
            fromY = offsetState.value.y - threshold,
            toY = constraints.maxHeight + offsetState.value.y + threshold,
        )
    }
}

private fun Modifier.customLayoutPointerInput(state: CustomLayoutState): Modifier {
    return pointerInput(Unit) {
        detectDragGestures { change, dragAmount ->
            change.consume()
            state.onDrag(IntOffset(dragAmount.x.toInt(), dragAmount.y.toInt()))
        }
    }
}

private interface CustomLayoutScope {
    fun items(items: List<CustomLayoutItem>, itemContent: CustomLayoutComposableItemContent)
}

private class CustomLayoutScopeImpl(): CustomLayoutScope {
    private val _items = mutableListOf<CustomLayoutItemContent>()
    val items: List<CustomLayoutItemContent> get() = _items

    override fun items(items: List<CustomLayoutItem>, itemContent: CustomLayoutComposableItemContent) {
        items.forEach {
            _items.add(CustomLayoutItemContent(it, itemContent))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
private class CustomLayoutItemProvider(
    private val itemsState: State<List<CustomLayoutItemContent>>
): LazyLayoutItemProvider {
    override val itemCount: Int get() = itemsState.value.size

    @Composable
    override fun Item(index: Int, key: Any) {
        itemsState.value.getOrNull(index)?.run {
            itemContent.invoke(item)
        }
    }

    fun getItemIndexesInRange(boundaries: CustomLayoutBoundaries): List<Int> {
        val result = mutableListOf<Int>()
        itemsState.value.forEachIndexed { i, itemContent ->
            val item = itemContent.item
            if (item.x in boundaries.fromX .. boundaries.toX
                && item.y in boundaries.fromY .. boundaries.toY) {
                result.add(i)
            }
        }
        return result
    }

    fun getItem(index: Int): CustomLayoutItem? {
        return itemsState.value.getOrNull(index)?.item
    }
}

@Composable
private fun rememberCustomLayoutItemProvider(
    scopeAction: CustomLayoutScope.() -> Unit
): CustomLayoutItemProvider {
    val state = remember {
        mutableStateOf(scopeAction)
    }.apply {
        value = scopeAction
    }

    return remember {
        CustomLayoutItemProvider(
            derivedStateOf {
                val layoutScope = CustomLayoutScopeImpl().apply(state.value)
                layoutScope.items
            }
        )
    }
}

private fun Placeable.PlacementScope.placeItem(
    state: CustomLayoutState,
    item: CustomLayoutItem,
    placeables: List<Placeable>,
) {
    val x = item.x - state.offsetState.value.x
    val y = item.y - state.offsetState.value.y

    placeables.forEach {
        it.placeRelative(x, y)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CustomLayoutFrame(
    modifier: Modifier = Modifier,
    state: CustomLayoutState = rememberCustomLayoutState(),
    content: CustomLayoutScope.() -> Unit
) {
    val itemProvider = rememberCustomLayoutItemProvider(content)

    LazyLayout(
        itemProvider = itemProvider,
        modifier = modifier
            .clipToBounds()
            .customLayoutPointerInput(state)
    ) {constraints ->
        val boundaries = state.getBoundaries(constraints)
        val indexes = itemProvider.getItemIndexesInRange(boundaries)

        val indexesWithPlaceables = indexes.associateWith {
            measure(it, Constraints())
        }
        layout(constraints.maxWidth, constraints.maxHeight) {
            indexesWithPlaceables.forEach {(index, placeables) ->
                itemProvider.getItem(index)?.let{
                    placeItem(state, it, placeables)
                }
            }
        }
    }
}

@Preview
@Composable
fun LazyLayoutPage() {
    val rows = remember {
        mutableStateListOf<CustomLayoutItem>()
    }

    LaunchedEffect(Unit) {
        for (i in 0 .. 10000000) {
            rows.add(
                CustomLayoutItem(
                    x= Random.nextInt(0, 1000000),
                    y= Random.nextInt(0, 1000000),
                )
            )
        }
    }

    CustomLayoutFrame {
        items(rows) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .padding(10.dp)
                    .background(Color.Blue)
            ) {
                Text(
                    text = "x: ${it.x} y: ${it.y}",
                    color = Color.White,
                )
            }
        }
    }
}