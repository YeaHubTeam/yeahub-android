package ru.yeahub.example.dynamicPreview

import androidx.compose.ui.tooling.preview.Preview

private const val STANDARD_SCREEN_WIDTH_DP = 360
private const val STANDARD_SCREEN_HEIGHT_DP = 640

@Preview(
    widthDp = STANDARD_SCREEN_WIDTH_DP,
    heightDp = STANDARD_SCREEN_HEIGHT_DP,
    showBackground = true
)
annotation class StandardScreenSizePreview()
