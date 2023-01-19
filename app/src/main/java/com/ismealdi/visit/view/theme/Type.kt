package com.ismealdi.visit.view.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.ismealdi.visit.R

val Satoshi = FontFamily(
    Font(R.font.satoshi_regular, FontWeight.W300),
    Font(R.font.satoshi_medium, FontWeight.W400),
    Font(R.font.satoshi_bold, FontWeight.Bold)
)

// Set of Material typography styles to start with
val Typography = Typography(
    displaySmall = TextStyle(
        fontFamily = Satoshi,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        lineHeight = 19.sp,
        letterSpacing = 0.5.sp
    ),

    bodySmall = TextStyle(
        fontFamily = Satoshi,
        fontWeight = FontWeight.W400,
        fontSize = 12.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.5.sp
    ),

    bodyLarge = TextStyle(
        fontFamily = Satoshi,
        fontWeight = FontWeight.W400,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),

    headlineMedium = TextStyle(
        fontFamily = Satoshi,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 34.sp,
        letterSpacing = 0.sp
    ),

    headlineSmall = TextStyle(
        fontFamily = Satoshi,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),

    titleLarge = TextStyle(
        fontFamily = Satoshi,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp,
    ),

    titleMedium = TextStyle(
        fontFamily = Satoshi,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp,
    ),

    titleSmall = TextStyle(
        fontFamily = Satoshi,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp,
    ),

    labelLarge = TextStyle(
        fontFamily = Satoshi,
        fontWeight = FontWeight.W400,
        fontSize = 12.sp,
        lineHeight = 15.sp,
        letterSpacing = 0.sp,
    ),

    labelMedium = TextStyle(
        fontFamily = Satoshi,
        fontWeight = FontWeight.W300,
        fontSize = 10.sp,
        lineHeight = 15.sp,
        letterSpacing = 0.sp,
    ),

    labelSmall = TextStyle(
        fontFamily = Satoshi,
        fontWeight = FontWeight.W300,
        fontSize = 8.sp,
        lineHeight = 15.sp,
        letterSpacing = 0.sp,
    )
)


val buttonTextMedium = TextStyle(
    fontFamily = Satoshi,
    fontWeight = FontWeight.Bold,
    fontSize = 14.sp,
    lineHeight = 15.sp,
    letterSpacing = 0.sp,
)

val topBarText = TextStyle(
    fontFamily = Satoshi,
    fontWeight = FontWeight.W400,
    fontSize = 14.sp,
    lineHeight = 15.sp,
    letterSpacing = 0.sp,
)

val topBarSubtitleText = TextStyle(
    fontFamily = Satoshi,
    fontWeight = FontWeight.W400,
    fontSize = 10.sp,
    lineHeight = 15.sp,
    letterSpacing = 0.sp,
)