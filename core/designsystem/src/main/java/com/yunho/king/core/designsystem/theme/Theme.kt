package com.yunho.king.core.designsystem.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView

private val DarkColorScheme = darkColorScheme(
    primary = KingPrimary,
    onPrimary = KingOnPrimary,
    background = KingDarkBackground,
    onBackground = KingOnBackground,
    surface = KingDarkSurface,
    onSurface = KingOnSurface,
    outline = KingOutline
)

private val LightColorScheme = lightColorScheme(
    primary = KingPrimary,
    onPrimary = KingOnPrimary,
    background = KingOnBackground,
    onBackground = KingDarkBackground,
    surface = KingOnSurface,
    onSurface = KingDarkBackground,
    outline = KingOutline
)

@Composable
fun KingTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = KingTypography,
        content = content
    )
}
