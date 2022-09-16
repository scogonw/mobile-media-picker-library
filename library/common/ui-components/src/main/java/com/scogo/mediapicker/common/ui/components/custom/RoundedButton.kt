package com.scogo.mediapicker.common.ui.components.custom

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import com.scogo.mediapicker.common.ui_theme.Dimens

@Composable
fun RoundedButton(
    modifier: Modifier,
    text: String,
    onActionClick: () -> Unit = {}
) {
    Button(
        modifier = modifier.clickable(
            interactionSource = MutableInteractionSource(),
            indication = rememberRipple(
                bounded = true,
                radius = Dp.Unspecified,
                color = Color.Unspecified
            )
        ) {},
        shape = RoundedCornerShape(Dimens.Seven),
        onClick = onActionClick
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.caption,
            fontWeight = FontWeight.Bold,
        )
    }
}