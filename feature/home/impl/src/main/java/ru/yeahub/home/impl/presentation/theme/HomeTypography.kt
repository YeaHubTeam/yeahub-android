package ru.yeahub.home.impl.presentation.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp
import ru.yeahub.core_ui.theme.Theme


val sfProText = FontFamily(
    Font(ru.yeahub.home.impl.R.font.sf_pro_text_semibold, FontWeight.SemiBold)
)
val arrowT: TextStyle = TextStyle(
    fontFamily = sfProText,
    fontWeight = FontWeight.SemiBold,
    fontSize = 16.sp,
    lineHeight = 24.sp,
    letterSpacing = 0.sp,
    platformStyle = PlatformTextStyle(includeFontPadding = false),
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None
    ),
)

object HomeTypography {

    val sectionTitle: TextStyle
        @Composable
        get() = Theme.typography.head5.copy(
            fontSize = 18.sp,
            lineHeight = 21.6.sp,
            letterSpacing = 0.sp
        )

    val questionProgressText: TextStyle
        @Composable
        get() = Theme.typography.body7.copy(
            fontSize = 16.sp,
            lineHeight = 16.8.sp,
            fontWeight = FontWeight.Medium
        )

    val questionTitleLarge: TextStyle
        @Composable
        get() = Theme.typography.body3.copy(
            lineHeight = 20.8.sp,
            fontWeight = FontWeight.Medium
        )

    val textArrow: TextStyle
        @Composable
        get() = arrowT



}
