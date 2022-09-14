package com.scogo.mediapicker.core.callback

import com.scogo.mediapicker.core.media.MediaData

interface MediaPickerCallback {
    fun onPick(list: List<MediaData>)
}