package com.scogo.mediapicker.core.media

data class MediaPickerConfiguration(
    var multipleAllowed: Boolean = true
){
    companion object {
        val EMPTY = MediaPickerConfiguration()
    }
}