package com.scogo.mediapicker.compose.media

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.scogo.mediapicker.core.data.api.MediaRepository
import com.scogo.mediapicker.core.media.MediaData
import com.scogo.mediapicker.core.request.PickerRequestData
import com.scogo.mediapicker.core.request.PickerRequestWorker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class MediaViewModel(
    private val repo: MediaRepository
): ViewModel() {
    private val worker = PickerRequestWorker.getInstance()
    private lateinit var requestData: PickerRequestData

    fun readRequestData() = requestData
    fun readCapturedMedia() = requestData.readCapturedMedia()

    private val _selectedMediaList = MutableStateFlow<List<MediaData>>(emptyList())
    val selectedMediaList: StateFlow<List<MediaData>> = _selectedMediaList

    private val selectedMediaMutex = Mutex()
    private val selectedMedia: MutableList<MediaData> = mutableListOf()

    fun initRequestData(workId: String?): Boolean {
        val workData = worker.getWork(workId)
        return if(workData != null) {
            requestData = workData
            true
        }else {
            false
        }
    }

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

    suspend fun selectMedia(mediaData: MediaData) {
        if (isMediaSelectionEnable(mediaData.id)) {
            var selected = mediaData.selected.value
            selected = !selected

            if(selected && !selectedMedia.contains(mediaData)) {
                selectedMedia.add(mediaData)
            } else if(selectedMedia.contains(mediaData)){
                selectedMedia.remove(mediaData)
            }
            mediaData.selected.value = selected
            changeSelectedMediaList(selectedMedia)
        }
    }

    private fun isMediaSelectionEnable(id: Long): Boolean  {
        return if(requestData.readPickerConfig().multipleAllowed) true
        else if(selectedMedia.isEmpty()) true
        else selectedMedia.size == 1 && selectedMedia[0].id == id
    }

    fun syncSelectedMediaList() {
        viewModelScope.launch {
            changeSelectedMediaList(requestData.readSelectedMedia())
        }
    }

    private suspend fun changeSelectedMediaList(list: List<MediaData>){
        requestData.changeSelectedMedia(list)
        selectedMediaMutex.withLock {
            _selectedMediaList.value = list.toMutableList()
        }
    }

    suspend fun writeToCapturedMedia(list: List<MediaData>) {
        requestData.changeCapturedMedia(list)
    }

    suspend fun clearCapturedMedia() {
        writeToCapturedMedia(emptyList())
    }

    suspend fun clearMediaSelection() {
        selectedMedia.clear()
        changeSelectedMediaList(emptyList())
    }


}