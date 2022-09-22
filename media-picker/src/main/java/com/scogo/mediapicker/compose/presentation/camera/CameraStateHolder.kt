package com.scogo.mediapicker.compose.presentation.camera

import androidx.camera.video.Recording
import com.scogo.mediapicker.compose.presentation.ComposeTimer

internal object CameraStateHolder {
    val timer = ComposeTimer.get()
    var recordingSession: Recording? = null
}