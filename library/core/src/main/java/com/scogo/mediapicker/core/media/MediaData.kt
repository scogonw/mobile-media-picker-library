package com.scogo.mediapicker.core.media

import android.net.Uri

data class MediaData(
    val id: Long,
    val uri: Uri?,
    val mimeType: String?,
    val displayName: String?,
    val dateTaken: Long?,
    val caption: String?,
    val bucketName: String?,
    val selected: Boolean,
) {
    companion object {
        val EMPTY = MediaData(
            id = 0,
            uri = null,
            mimeType = null,
            displayName = null,
            dateTaken = null,
            caption = null,
            bucketName = null,
            selected = false
        )
    }
}