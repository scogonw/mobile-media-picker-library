package com.scogo.mediapicker.compose.core.media

import android.net.Uri
import kotlinx.coroutines.flow.MutableStateFlow

data class MediaData(
    val id: Long,
    var uri: Uri?,
    var mimeType: String?,
    val mediaType: Int?,
    val displayName: String?,
    val date: String?,
    val dateTaken: Long?,
    var caption: String?,
    val bucketName: String?,
    internal var selected: MutableStateFlow<Boolean> = MutableStateFlow(false),
) {
    companion object {
        val EMPTY = MediaData(
            id = 0,
            uri = null,
            mimeType = null,
            mediaType = null,
            displayName = null,
            dateTaken = null,
            date = null,
            caption = null,
            bucketName = null
        )
        fun create(uri: Uri?, mime: String?): MediaData {
            return EMPTY.also {
                it.uri = uri
                it.mimeType = mime
            }
        }
    }
}