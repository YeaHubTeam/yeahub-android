package ru.yeahub.core_ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class Colors(
    val purple900: Color,
    val purple800: Color,
    val purple700: Color,
    val purple600: Color,
    val purple500: Color,
    val purple400: Color,
    val purple300: Color,
    val purple200: Color,
    val purple100: Color,

    val red900: Color,
    val red800: Color,
    val red700: Color,
    val red600: Color,
    val red500: Color,
    val red400: Color,
    val red300: Color,
    val red200: Color,
    val red100: Color,

    val yellow900: Color,
    val yellow800: Color,
    val yellow700: Color,
    val yellow600: Color,

    val green900: Color,
    val green800: Color,
    val green700: Color,
    val green600: Color,
    val green007: Color,

    val black900: Color,
    val black800: Color,
    val black700: Color,
    val black600: Color,
    val black500: Color,
    val black400: Color,
    val black300: Color,
    val black200: Color,
    val black100: Color,
    val black50: Color,
    val black25: Color,

    val white900: Color,

    val mainShadow: Color,
)

val colors = Colors(
    purple900 = Purple900,
    purple800 = Purple800,
    purple700 = Purple700,
    purple600 = Purple600,
    purple500 = Purple500,
    purple400 = Purple400,
    purple300 = Purple300,
    purple200 = Purple200,
    purple100 = Purple100,
    red900 = Red900,
    red800 = Red800,
    red700 = Red700,
    red600 = Red600,
    red500 = Red500,
    red400 = Red400,
    red300 = Red300,
    red200 = Red200,
    red100 = Red100,
    yellow900 = Yellow900,
    yellow800 = Yellow800,
    yellow700 = Yellow700,
    yellow600 = Yellow600,
    green900 = Green900,
    green800 = Green800,
    green700 = Green700,
    green600 = Green600,
    green007 = Green007,
    black900 = Black900,
    black800 = Black800,
    black700 = Black700,
    black600 = Black600,
    black500 = Black500,
    black400 = Black400,
    black300 = Black300,
    black200 = Black200,
    black100 = Black100,
    black50 = Black50,
    black25 = Black25,
    white900 = White900,
    mainShadow = MainShadow,
)

val LocalAppColors = staticCompositionLocalOf<Colors> {
    colors
}