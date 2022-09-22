package com.scogo.mediapicker.compose.core.data.impl

import android.content.Context
import androidx.paging.PagingSource
import com.scogo.mediapicker.compose.core.data.api.MediaDataSource
import com.scogo.mediapicker.compose.core.data.api.MediaRepository
import com.scogo.mediapicker.compose.core.media.MediaData
import com.scogo.mediapicker.compose.core.media.createMediaCursor
import com.scogo.mediapicker.compose.core.media.fetchMedia
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class MediaRepositoryImpl(
    private val context: Context
): MediaRepository {

    override suspend fun getCount(): Int {
        val cursor = context.createMediaCursor(Int.MAX_VALUE,0) ?: return 0
        val count = cursor.count
        cursor.close()
        return count
    }

    override suspend fun getByOffset(offset: Int): MediaData? {
        return context.fetchMedia(1,offset).firstOrNull()
    }

    override fun getMediaPagingSource(): PagingSource<Int, MediaData> {
        return MediaDataSource { limit, offset ->
            withContext(Dispatchers.IO) {
                context.fetchMedia(limit, offset)
            }
        }
    }

}