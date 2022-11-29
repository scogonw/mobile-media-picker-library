package com.scogo.mediapicker.compose.core.data.api

import androidx.paging.PagingSource
import com.scogo.mediapicker.compose.core.media.MediaData
import com.scogo.mediapicker.compose.core.media.MimeTypes

internal interface MediaRepository {
    suspend fun getCount(): Int
    suspend fun getByOffset(offset: Int): MediaData?
    fun getMediaPagingSource(mimeType: MimeTypes): PagingSource<Int, MediaData>
}