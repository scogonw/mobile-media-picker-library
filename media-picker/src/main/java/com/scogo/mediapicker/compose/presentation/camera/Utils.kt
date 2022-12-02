package com.scogo.mediapicker.compose.presentation.camera

sealed class CameraCaptureType {
    object Image: CameraCaptureType()
    object Video: CameraCaptureType()
}