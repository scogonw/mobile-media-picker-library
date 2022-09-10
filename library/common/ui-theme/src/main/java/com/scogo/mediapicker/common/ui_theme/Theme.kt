package com.scogo.mediapicker.common.ui_theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = Blue200,
    primaryVariant = Blue500,
    secondary = Orange200
)
private val LightColorPalette = lightColors(
    primary = Blue200,
    primaryVariant = Blue500,
    secondary = Orange200,
)

@Composable
fun ScogoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val uiController = rememberSystemUiController()
    uiController.setStatusBarColor(
        color = Color.Transparent
    )

    //we don't need dark theme
    val colors = if(darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = LightColorPalette,
        shapes = Shapes,
        typography = Typography,
        content = content
    )
}