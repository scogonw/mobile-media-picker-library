package com.scogo.mediapicker.core.media

data class MediaPickerConfiguration(
    var multipleAllowed: Boolean = true,
    var captionMandatory: Boolean = false,
){
    companion object {
        val EMPTY = MediaPickerConfiguration()
    }
}