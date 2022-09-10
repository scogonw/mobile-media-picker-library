package com.scogo.mediapicker.core.media

sealed class MimeTypes(val name: String) {
    object IMAGE: MimeTypes("image")
    object VIDEO: MimeTypes("video")
}
