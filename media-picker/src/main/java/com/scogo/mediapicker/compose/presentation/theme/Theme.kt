package com.scogo.mediapicker.compose.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.scogo.mediapicker.compose.presentation.theme.*

private val DarkColorPalette = darkColors(
    primary = LightBlue,
    primaryVariant = Blue200,
    secondary = Orange200
)
private val LightColorPalette = lightColors(
    primary = LightBlue,
    primaryVariant = Blue200,
    secondary = Orange200,
)

@Composable
internal fun ScogoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val uiController = rememberSystemUiController()
    uiController.setStatusBarColor(
        color = Color.Black
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