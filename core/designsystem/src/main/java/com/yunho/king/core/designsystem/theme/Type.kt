package com.yunho.king.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.yunho.king.core.designsystem.R

val NotoSansKr = FontFamily(
    Font(R.font.notosanskr_thin, FontWeight.Thin),
    Font(R.font.notosanskr_light, FontWeight.Light),
    Font(R.font.notosanskr_regular, FontWeight.Normal),
    Font(R.font.notosanskr_medium, FontWeight.Medium),
    Font(R.font.notosanskr_bold, FontWeight.Bold),
    Font(R.font.notosanskr_black, FontWeight.Black),
)

val KingTypography = Typography(
    displaySmall = TextStyle(
        fontFamily = NotoSansKr,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = NotoSansKr,
        fontWeight = FontWeight.Bold,
        fontSize = 26.sp
    ),
    titleLarge = TextStyle(
        fontFamily = NotoSansKr,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    ),
    titleMedium = TextStyle(
        fontFamily = NotoSansKr,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = NotoSansKr,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = NotoSansKr,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    bodySmall = TextStyle(
        fontFamily = NotoSansKr,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
    labelMedium = TextStyle(
        fontFamily = NotoSansKr,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp
    ),
    labelSmall = TextStyle(
        fontFamily = NotoSansKr,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp
    ),
)
