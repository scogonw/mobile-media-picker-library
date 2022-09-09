package com.scogo.mediapicker.core.data.api

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.scogo.mediapicker.core.media.MediaData

class MediaDataSource(
    private val onFetch: suspend (limit: Int, offset: Int) -> List<MediaData>
): PagingSource<Int, MediaData>() {

    override fun getRefreshKey(state: PagingState<Int, MediaData>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MediaData> {
        val pageNumber = params.key ?: 0
        val pageSize = params.loadSize
        val mediaList = onFetch.invoke(pageSize,pageNumber * pageSize)
        val prevKey = if(pageNumber > 0) pageNumber.minus(1) else null
        val nextKey = if(mediaList.isNotEmpty()) pageNumber.plus(1) else null

        return LoadResult.Page(
            data = mediaList,
            prevKey = prevKey,
            nextKey = nextKey
        )
    }

}