package com.scogo.mediapicker.compose.camera

import androidx.camera.video.Recording
import com.scogo.mediapicker.compose.util.ComposeTimer

internal object CameraStateHolder {
    val timer = ComposeTimer.get()
    var recordingSession: Recording? = null
}