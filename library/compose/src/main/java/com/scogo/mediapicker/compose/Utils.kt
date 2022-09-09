package com.scogo.mediapicker.compose

import android.app.Activity
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import com.scogo.mediapicker.compose.home.HomeActivity
import com.scogo.mediapicker.compose.media.MediaViewModel

@Composable
internal fun composeActivity(): Activity {
    val context = LocalContext.current
    return (context as Activity)
}

@Composable
internal fun activityMediaViewModel(): MediaViewModel {
    val context = LocalContext.current
    return (context as HomeActivity).mediaViewModel
}

@Composable
internal fun activitySharedViewModel(): SharedViewModel {
    val context = LocalContext.current
    return (context as HomeActivity).viewModel
}

@Composable
internal fun Modifier.OnSwipe(
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