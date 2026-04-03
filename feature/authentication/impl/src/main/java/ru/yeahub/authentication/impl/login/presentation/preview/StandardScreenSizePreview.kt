package ru.yeahub.authentication.impl.login.presentation.preview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Стандартный размер preview для экранов логина.
 */
val StandardPreviewWidth = 360.dp
val StandardPreviewHeight = 640.dp

/**
 * Контейнер с фиксированным размером для preview.
 */
@Composable
fun StandardScreenSizePreview(
    modifier: Modifier,
    width: Dp,
    height: Dp,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier.size(
            width = width,
            height = height,
        ),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}