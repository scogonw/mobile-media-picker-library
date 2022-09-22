package com.scogo.mediapicker.compose.core.request

import com.scogo.mediapicker.compose.core.callback.MediaPickerCallback
import com.scogo.mediapicker.compose.core.media.MediaPickerConfiguration

internal interface PickerRequestWorkerCallback {
    fun enqueue(config: MediaPickerConfiguration, callback: MediaPickerCallback): String
    fun dequeue(id: String): String
    fun getWork(id: String?): PickerRequestData?
}