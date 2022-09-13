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
import kotlinx.coroutines.withContext

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

    suspend fun selectMedia(
        mediaData: MediaData
    ): Boolean? = withContext(Dispatchers.IO){
        val selected: Boolean?
        if (isMediaSelectionEnable(mediaData.id)) {
            val media = selectedMedia.find { it.id == mediaData.id }
            if(media == null) {
                selected = true
                mediaData.selected = selected
                selectedMedia.add(mediaData)
            }else {
                selected = false
                selectedMedia.remove(mediaData)
            }
            _selectedMediaList.value = selectedMedia
        }else {
            selected = null
        }
        selected
    }

    private fun isMediaSelectionEnable(
        id: Long
    ): Boolean  {
        return if(config.multipleAllowed) true
        else if(selectedMedia.isEmpty()) true
        else selectedMedia.size == 1 && selectedMedia[0].id == id && selectedMedia[0].selected
    }

    fun isMediaSelected(id: Long, ): Boolean {
        return if(selectedMedia.isNotEmpty()) {
            selectedMedia.find { it.id == id }?.selected ?: false
        }else {
            false
        }
    }

    fun clearMediaSelection() {
        viewModelScope.launch(Dispatchers.IO) {
            selectedMedia.clear()
            _selectedMediaList.value = selectedMedia
        }
    }


}