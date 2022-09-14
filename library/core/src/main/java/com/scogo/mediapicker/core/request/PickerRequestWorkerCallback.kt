package com.scogo.mediapicker.core.request

import com.scogo.mediapicker.core.callback.MediaPickerCallback
import com.scogo.mediapicker.core.media.MediaPickerConfiguration

interface PickerRequestWorkerCallback {
    fun enqueue(config: MediaPickerConfiguration, callback: MediaPickerCallback): String
    fun dequeue(id: String): String
    fun getWork(id: String?): PickerRequestData?
}