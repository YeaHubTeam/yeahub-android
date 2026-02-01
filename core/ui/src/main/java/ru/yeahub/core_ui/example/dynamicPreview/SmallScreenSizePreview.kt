package ru.yeahub.core_ui.example.dynamicPreview

import androidx.compose.ui.tooling.preview.Preview

private const val SMALL_SCREEN_WIDTH_DP = 320
private const val SMALL_SCREEN_HEIGHT_DP = 480

@Preview(
    widthDp = SMALL_SCREEN_WIDTH_DP,
    heightDp = SMALL_SCREEN_HEIGHT_DP,
    showBackground = true,
)
annotation class SmallScreenSizePreview()
