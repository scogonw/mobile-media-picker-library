package com.scogo.mediapicker.compose.core.media

internal sealed class MimeTypes(
    val name: String
) {
    object IMAGE: MimeTypes("image")
    object VIDEO: MimeTypes("video")
    object IMAGE_JPEG: MimeTypes("image/jpeg")
    object VIDEO_MP4: MimeTypes("video/mp4")
}
