package ru.yeahub.authentication.impl.login.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun StandardScreenSizePreview(
    modifier: Modifier = Modifier,
    width: Dp = 360.dp,
    height: Dp = 640.dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier.size(width, height),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}