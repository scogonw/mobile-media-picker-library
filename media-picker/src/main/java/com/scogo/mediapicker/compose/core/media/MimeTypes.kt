package com.scogo.mediapicker.compose.core.media

sealed class MimeTypes(
    val name: String
) {
    object NONE: MimeTypes("none")
    object IMAGE: MimeTypes("image")
    object VIDEO: MimeTypes("video")
    internal object IMAGE_JPEG: MimeTypes("image/jpeg")
    internal object VIDEO_MP4: MimeTypes("video/mp4")
}
