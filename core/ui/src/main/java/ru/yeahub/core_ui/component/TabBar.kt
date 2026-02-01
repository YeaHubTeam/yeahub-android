package ru.yeahub.core_ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.yeahub.core_ui.example.dynamicPreview.StandardScreenSizePreview
import ru.yeahub.core_ui.theme.Theme

@Immutable
data class CoreTopTabsColors(
    val containerColor: Color,
    val indicatorColor: Color,
    val selectedTextColor: Color,
    val unselectedTextColor: Color,
)

object CoreTopTabsDefaults {
    @Composable
    fun colors(
        containerColor: Color = Theme.colors.white900,
        indicatorColor: Color = Theme.colors.purple700,
        selectedTextColor: Color = Theme.colors.black900,
        unselectedTextColor: Color = Theme.colors.black500,
    ) = CoreTopTabsColors(
        containerColor = containerColor,
        indicatorColor = indicatorColor,
        selectedTextColor = selectedTextColor,
        unselectedTextColor = unselectedTextColor,
    )
}

@Composable
fun CoreTopTabs(
    tabs: List<String>,
    selectedIndex: Int,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    colors: CoreTopTabsColors = CoreTopTabsDefaults.colors(),
    edgePadding: Dp = 0.dp,
    tabSpacing: Dp = 0.dp,
    indicatorHeight: Dp = 2.dp,
    indicatorHorizontalPadding: Dp = 16.dp,
) {
    ScrollableTabRow(
        selectedTabIndex = selectedIndex,
        modifier = modifier,
        containerColor = colors.containerColor,
        contentColor = colors.selectedTextColor,
        edgePadding = edgePadding,
        divider = { HorizontalDivider(thickness = 0.dp) },
        indicator = { positions ->
            val pos = positions[selectedIndex]
            Box(
                Modifier
                    .tabIndicatorOffset(pos)
                    .padding(horizontal = indicatorHorizontalPadding)
                    .height(indicatorHeight)
                    .background(colors.indicatorColor),
            )
        },
    ) {
        tabs.forEachIndexed { index, title ->
            val isSelected = index == selectedIndex
            Tab(
                selected = isSelected,
                onClick = { onSelected(index) },
                text = {
                    Text(
                        text = title,
                        color = if (isSelected) colors.selectedTextColor else colors.unselectedTextColor,
                        style = Theme.typography.head6,
                        maxLines = 1,
                        softWrap = false,
                        overflow = TextOverflow.Visible,
                    )
                },
            )
        }
    }
}

data class YhTabsParams(
    val items: List<String>,
    val selectedIndex: Int,
)

class YhTabsParamsProvider : PreviewParameterProvider<YhTabsParams> {
    override val values = sequenceOf(
        YhTabsParams(listOf("Личная информация", "Обо мне", "Навыки"), 0),
        YhTabsParams(
            listOf(
                "Tab 1",
                "Очень длинный таб",
                "Очень длинный таб",
                "Очень длинный таб",
                "Tab 1",
                "Tab 1",
            ),
            2,
        ),
    )
}

@StandardScreenSizePreview
@Composable
fun YhTabsPreview(
    @PreviewParameter(YhTabsParamsProvider::class) p: YhTabsParams,
) {
    var selected by remember { mutableIntStateOf(p.selectedIndex) }

    Box(
        Modifier
            .background(Color.White)
            .padding(0.dp),
    ) {
        CoreTopTabs(
            tabs = p.items,
            selectedIndex = selected,
            onSelected = { selected = it },
        )
    }
}