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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class MediaViewModel(
    private val repo: MediaRepository,
    private val config: MediaPickerConfiguration,
): ViewModel() {
    private val _selectedMediaList = MutableStateFlow<List<MediaData>>(emptyList())
    val selectedMediaList: StateFlow<List<MediaData>> = _selectedMediaList

    private val selectedMedia: MutableList<MediaData> = mutableListOf()

    fun getMediaList(): Flow<PagingData<MediaData>> {
        return Pager(
            config = PagingConfig(
                pageSize = 100,
                prefetchDistance = 50,
                initialLoadSize = 100,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                repo.getMediaPagingSource()
            }
        ).flow.cachedIn(viewModelScope)
    }

    fun selectMedia(
        mediaData: MediaData
    ) {
        if (isMediaSelectionEnable(mediaData.id)) {
            var selected = mediaData.selected.value
            selected = !selected

            if(selected && !selectedMedia.contains(mediaData)) {
                selectedMedia.add(mediaData)
            } else if(selectedMedia.contains(mediaData)){
                selectedMedia.remove(mediaData)
            }
            mediaData.selected.value = selected
            _selectedMediaList.value = selectedMedia
        }
    }

    private fun isMediaSelectionEnable(
        id: Long
    ): Boolean  {
        return if(config.multipleAllowed) true
        else if(selectedMedia.isEmpty()) true
        else selectedMedia.size == 1 && selectedMedia[0].id == id
    }

    fun clearMediaSelection() {
        viewModelScope.launch(Dispatchers.IO) {
            selectedMedia.clear()
            _selectedMediaList.value = selectedMedia
        }
    }


}