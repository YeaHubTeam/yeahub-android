package ru.yeahub.core_ui.theme

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

@Composable
fun YeaHubTheme(
    colors: Colors = Theme.colors,
    typography: Typography = Theme.typography,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        typography = MaterialTypography
    ) {
        CompositionLocalProvider(
            LocalAppColors provides colors,
            LocalAppTypography provides typography,
            LocalContentColor provides colors.white900,
            content = content
        )
    }
}

internal object Theme {
    val colors: Colors
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColors.current

    val typography: Typography
        @Composable
        @ReadOnlyComposable
        get() = LocalAppTypography.current
}