package com.scogo.mediapicker.compose.camera

import android.view.MotionEvent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInteropFilter

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CameraActionIcon(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    val isPressed = remember { mutableStateOf(false) }
    val scaleAnim by animateFloatAsState(if(isPressed.value) 0.8f else 1f)

    IconButton(
        modifier = modifier
            .scale(scaleAnim)
            .pointerInteropFilter {
                when (it.action) {
                    MotionEvent.ACTION_DOWN -> {
                        isPressed.value = true
                        onClick()
                    }
                    MotionEvent.ACTION_UP -> {
                        isPressed.value = false
                    }
                }
                true
            },
        onClick = { },
        content = content
    )

}