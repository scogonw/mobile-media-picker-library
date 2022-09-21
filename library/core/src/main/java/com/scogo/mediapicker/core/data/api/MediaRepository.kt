package com.scogo.mediapicker.core.data.api

import androidx.paging.PagingSource
import com.scogo.mediapicker.core.media.MediaData

interface MediaRepository {
    suspend fun getCount(): Int
    suspend fun getByOffset(offset: Int): MediaData?
    fun getMediaPagingSource(): PagingSource<Int, MediaData>
}