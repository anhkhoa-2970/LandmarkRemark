package com.test.landmarkremark.presentation.ui.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val backGroundColor =  Brush.linearGradient(
    colors = listOf(
        Color(0xFFcaf2f5),
        Color(0xFFFFFFFF),
        Color(0xFFFFFFFF),
        Color(0xFFFFFFFF),
        Color(0xFFcaf2f5)
    ),
    start = Offset.Zero,
    end = Offset.Infinite,
    tileMode = androidx.compose.ui.graphics.TileMode.Repeated,
)

val colorStatusBar = Color(0xFFCAF2F5)
val colorMain = Color(0x13A1BE)