package com.scogo.mediapicker.compose

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun Modifier.OnSwipe(
    onLeft: () -> Unit = {},
    onRight: () -> Unit = {},
    onUp: () -> Unit = {},
    onDown: () -> Unit = {}
): Modifier {
    pointerInput(Unit) {
        detectDragGestures { change, dragAmount ->
            change.consume()
            val (x,y) = dragAmount
            when {
                x > 0 -> {
                    //right
                    onRight()
                }
                x < 0 -> {
                    //left
                    onLeft()
                }
            }
            when {
                y > 0 -> {
                    //down
                    onDown()
                }
                y < 0 -> {
                    //up
                    onUp()
                }
            }
        }
    }
    return this
}