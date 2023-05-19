package com.scogo.mediapicker.compose.core.media

data class MediaPickerConfiguration(
    var multipleAllowed: Boolean = true,
    var mimeType: MimeTypes = MimeTypes.NONE,
    var captionMandatory: Boolean = false,
    var captionEnabled: Boolean = false,
)