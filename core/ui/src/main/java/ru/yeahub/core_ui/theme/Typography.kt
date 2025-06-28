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
    Font(R.font.manrope_bold, FontWeight.Bold),
)

data class Typography(
    // Head
    val head1: TextStyle = TextStyle(
        fontFamily = manrope,
        fontWeight = FontWeight.Medium,
        fontSize = 60.sp,
        lineHeight = 114.sp,
    ),
    val head2: TextStyle = TextStyle(
        fontFamily = manrope,
        fontWeight = FontWeight.Medium,
        fontSize = 40.sp,
        lineHeight = 115.sp,
    ),
    val head3: TextStyle = TextStyle(
        fontFamily = manrope,
        fontWeight = FontWeight.Medium,
        fontSize = 34.sp,
        lineHeight = 115.sp,
    ),
    val head4: TextStyle = TextStyle(
        fontFamily = manrope,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 127.sp,
    ),
    //Body
    val body1: TextStyle = TextStyle(
        fontFamily = manrope,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 120.sp,
    ),
    val bodyAccent: TextStyle = TextStyle(
        fontFamily = manrope,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 120.sp,
    ),
    //body2
    val body2: TextStyle = TextStyle(
        fontFamily = manrope,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 120.sp,
    ),
    val body2Accent: TextStyle = TextStyle(
        fontFamily = manrope,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 120.sp,
    ),
    val body2Strong: TextStyle = TextStyle(
        fontFamily = manrope,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 120.sp,
    ),
    //body3
    val body3: TextStyle = TextStyle(
        fontFamily = manrope,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 130.sp,
    ),
    val body3Accent: TextStyle = TextStyle(
        fontFamily = manrope,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 130.sp,
    ),
    val body3Strong: TextStyle = TextStyle(
        fontFamily = manrope,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 130.sp,
    ),
    //body4
    val body4: TextStyle = TextStyle(
        fontFamily = manrope,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 120.sp,
    ),
    //body5
    val body5: TextStyle = TextStyle(
        fontFamily = manrope,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 120.sp,
    ),
    val body5Accent: TextStyle = TextStyle(
        fontFamily = manrope,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 120.sp,
    ),
    val body5Strong: TextStyle = TextStyle(
        fontFamily = manrope,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 120.sp,
    ),
    //body6
    val body6: TextStyle = TextStyle(
        fontFamily = manrope,
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp,
        lineHeight = 120.sp,
    )
)

val LocalAppTypography = staticCompositionLocalOf {
    ru.yeahub.core_ui.theme.Typography()
}

val MaterialTypography = Typography()