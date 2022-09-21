package com.scogo.mediapicker.common.ui.components.custom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.scogo.mediapicker.common.ui_theme.Dimens

@Composable
fun Chip(
    modifier: Modifier,
    text: String,
) {
    Box(
        modifier = modifier,
        content = {
            Text(
                modifier = Modifier
                    .align(Alignment.Center)
                    .clip(CircleShape)
                    .background(Color.Red.copy(alpha = 0.5f))
                    .padding(Dimens.One)
                ,
                text = text,
                color = Color.White
            )
        }
    )
}