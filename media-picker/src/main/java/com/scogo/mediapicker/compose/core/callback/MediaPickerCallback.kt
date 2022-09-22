package com.scogo.mediapicker.compose.core.callback

import com.scogo.mediapicker.compose.core.media.MediaData

interface MediaPickerCallback {
    fun onPick(list: List<MediaData>)
}