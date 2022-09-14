package com.scogo.mediapicker.compose.util

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput

@OptIn(ExperimentalMaterialApi::class)
suspend fun ModalBottomSheetState.animatedHide() {
    animateTo(ModalBottomSheetValue.Hidden)
}
@OptIn(ExperimentalMaterialApi::class)
suspend fun ModalBottomSheetState.animatedShow() {
    animateTo(ModalBottomSheetValue.Expanded)
}

fun Modifier.onSwipe(
    onLeft: () -> Unit = {},
    onRight: () -> Unit = {},
    onUp: () -> Unit = {},
    onDown: () -> Unit = {}
): Modifier = pointerInput(Unit) {
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