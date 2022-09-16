package com.scogo.mediapicker.common.ui.components.camera

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import com.scogo.mediapicker.common.ui.components.util.DetectGestures

@Composable
fun CameraActionIcon(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onHold: (released: Boolean) -> Unit = {},
    content: @Composable () -> Unit = {},
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed = interactionSource.collectIsPressedAsState()

    val scaleAnim by animateFloatAsState(if(isPressed.value) 0.8f else 1f)

    DetectGestures(
        pressedState = isPressed,
        onClick = onClick,
        onHold = onHold
    )

    IconButton(
        modifier = modifier.scale(scaleAnim),
        interactionSource = interactionSource,
        onClick = {},
        content = content
    )
}