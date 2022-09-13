package com.scogo.mediapicker.common.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CircleCheckbox(
    modifier: Modifier = Modifier,
    checked: Boolean = false,
    enabled: Boolean = true,
    onCheckedChange: (Boolean) -> Unit = { }
) {
    val icon = if(checked) Icons.Filled.CheckCircle else Icons.Outlined.Circle
    val tint = if(checked) Color.Blue else Color.White
    IconButton(
        modifier = modifier.offset(x = 4.dp, y = 4.dp),
        enabled = enabled,
        onClick = {
            onCheckedChange(!checked)
        },
        content =  {
            Icon(
                modifier = modifier.background(
                    color = Color.Transparent,
                    shape = CircleShape
                ),
                tint = tint,
                imageVector = icon,
                contentDescription = null
            )
        }
    )
}

@Preview
@Composable
private fun Preview() {
    CircleCheckbox(
        checked = true
    )
}