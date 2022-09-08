package com.scogo.mediapicker.presentation.camera

import android.media.MediaActionSound
import java.io.File

data class CameraDataHolder(
    val outputDirectory: File,
    val sound: MediaActionSound
)