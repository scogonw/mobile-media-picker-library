package com.scogo.mediapicker.compose.media

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.scogo.mediapicker.core.data.api.MediaRepository
import com.scogo.mediapicker.core.media.MediaData
import com.scogo.mediapicker.core.media.MediaPickerConfiguration
import kotlinx.coroutines.flow.Flow

internal class MediaViewModel(
    private val repo: MediaRepository,
    private val config: MediaPickerConfiguration,
): ViewModel() {

    fun getMediaList(): Flow<PagingData<MediaData>> {
        return Pager(
            config = PagingConfig(
                pageSize = 100,
                initialLoadSize = 100,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                repo.getMediaPagingSource()
            }
        ).flow.cachedIn(viewModelScope)
    }

}