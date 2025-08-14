package ru.yeahub.selection_specializations.impl.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.valentinilk.shimmer.shimmer
import ru.yeahub.selection_specializations.impl.ui.SpecializationScreen.Companion.FIGMA_HORIZONTAL_PADDING
import ru.yeahub.selection_specializations.impl.ui.SpecializationScreen.Companion.FIGMA_VERTICAL_TITLE_PADDING

@Composable
fun SpecializationsLoadingScreen(
    modifier: Modifier = Modifier,
    padding: PaddingValues = PaddingValues()
) {
    Box(
        modifier = modifier
            .padding(padding)
    ) {
        SpecializationButton(
            title = "Loading specializations...",
            onSpecialClick = {},
            modifier = modifier
                .clickable(
                    enabled = false,
                    interactionSource = null,
                    indication = null,
                    onClick = { },
                )
                .shimmer()
        )
    }
}

@Preview
@Composable
fun SpecializationsLoadingReview() {
    val padding = PaddingValues(
        vertical = FIGMA_VERTICAL_TITLE_PADDING,
        horizontal = FIGMA_HORIZONTAL_PADDING
    )
    SpecializationsLoadingScreen(padding = padding)
}