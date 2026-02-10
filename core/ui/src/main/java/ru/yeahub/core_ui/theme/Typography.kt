package ru.yeahub.core_ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ru.yeahub.ui.R

private val manrope = FontFamily(
    Font(R.font.manrope_regular, FontWeight.Normal),
    Font(R.font.manrope_medium, FontWeight.Medium),
    Font(R.font.manrope_semibold, FontWeight.SemiBold),
    Font(R.font.manrope_bold, FontWeight.Bold)
)

private val sfProDisplay = FontFamily(
    Font(R.font.sf_pro_display_medium, FontWeight.Medium),
)

private val sfProText = FontFamily(
    Font(R.font.sf_pro_text_regular, FontWeight.Normal)
)

data class Typography(
    // Head
    val head1: TextStyle = TextStyle(
        fontFamily = manrope,
        fontWeight = FontWeight.Medium,
        fontSize = 60.sp,
        lineHeight = 68.sp,
    ),
    val head2: TextStyle = TextStyle(
        fontFamily = manrope,
        fontWeight = FontWeight.Medium,
        fontSize = 40.sp,
        lineHeight = 46.sp,
    ),
    val head3: TextStyle = TextStyle(
        fontFamily = manrope,
        fontWeight = FontWeight.Medium,
        fontSize = 34.sp,
        lineHeight = 39.sp,
    ),
    val head4: TextStyle = TextStyle(
        fontFamily = manrope,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        lineHeight = 23.sp,
    ),
    val head5: TextStyle = TextStyle(
        fontFamily = sfProDisplay,
        fontWeight = FontWeight.Medium,
        fontSize = 28.sp,
        lineHeight = (28 * 1.15).sp,
    ),
    val head5Alt: TextStyle = TextStyle(
        fontFamily = sfProDisplay,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp,
        lineHeight = 26.sp,
    ),
    val head6: TextStyle = TextStyle(
        fontFamily = sfProDisplay,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),
    //Body
    val body1: TextStyle = TextStyle(
        fontFamily = manrope,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 14.sp,
    ),
    val bodyAccent: TextStyle = TextStyle(
        fontFamily = manrope,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 14.sp,
    ),
    val bodyStrong: TextStyle = TextStyle(
        fontFamily = manrope,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = 14.sp,
    ),
    //body2
    val body2: TextStyle = TextStyle(
        fontFamily = manrope,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 17.sp,
    ),
    val body2Accent: TextStyle = TextStyle(
        fontFamily = manrope,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 17.sp,
    ),
    val body2Strong: TextStyle = TextStyle(
        fontFamily = manrope,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 17.sp,
    ),
    //body3
    val body3: TextStyle = TextStyle(
        fontFamily = manrope,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 21.sp,
    ),
    val body3Accent: TextStyle = TextStyle(
        fontFamily = manrope,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 21.sp,
    ),
    val body3Strong: TextStyle = TextStyle(
        fontFamily = manrope,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 21.sp,
    ),
    //body4
    val body4: TextStyle = TextStyle(
        fontFamily = manrope,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 22.sp,
    ),
    //body5
    val body5: TextStyle = TextStyle(
        fontFamily = manrope,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 24.sp,
    ),
    val body5Accent: TextStyle = TextStyle(
        fontFamily = manrope,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        lineHeight = 24.sp,
    ),
    val body5Strong: TextStyle = TextStyle(
        fontFamily = manrope,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 24.sp,
    ),
    //body6
    val body6: TextStyle = TextStyle(
        fontFamily = manrope,
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp,
        lineHeight = 29.sp,
    ),
    val body7: TextStyle = TextStyle(
        fontFamily = sfProText,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 21.sp,
    ),
)

val LocalAppTypography = staticCompositionLocalOf {
    ru.yeahub.core_ui.theme.Typography()
}

val MaterialTypography = Typography()