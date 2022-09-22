package com.scogo.mediapicker.compose.presentation.media

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.scogo.mediapicker.compose.core.data.api.MediaRepository
import com.scogo.mediapicker.compose.core.media.MediaData
import com.scogo.mediapicker.compose.core.request.PickerRequestData
import com.scogo.mediapicker.compose.core.request.PickerRequestWorker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class MediaViewModel(
    private val repo: MediaRepository
): ViewModel() {
    var cropMedia: MediaData? = null
    var mediaIndex: Int = 0

    private val _uiState = MutableStateFlow(MediaUiState.EMPTY)
    val uiState: StateFlow<MediaUiState> get() = _uiState

    private val worker = PickerRequestWorker.getInstance()
    private lateinit var requestData: PickerRequestData

    fun readRequestData() = requestData
    fun captionMandatory() = readRequestData().readPickerConfig().captionMandatory
    fun readCapturedMedia() = requestData.readCapturedMedia()

    private val _selectedMediaList = MutableStateFlow<List<MediaData>>(emptyList())
    val selectedMediaList: StateFlow<List<MediaData>> get() = _selectedMediaList

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
                repo.getMediaPagingSource(
                    mimeType = readRequestData().readPickerConfig().mimeType
                )
            }
        ).flow.cachedIn(viewModelScope)
    }

    fun syncSelectedMediaList() {
        viewModelScope.launch {
            changeSelectedMediaList(
                list = requestData.readSelectedMedia()
            )
        }
    }

    suspend fun updateMedia(media: MediaData) : Int{
        return try {
            val list = _selectedMediaList.value.toMutableList()
            val index = list.indexOfFirst { i -> i.id == media.id }
            if(index != -1) {
                list[index] = media
                changeSelectedMediaList(list)
            }
            index
        }catch (e: Exception) {
            -1
        }
    }

    fun isCaptionsEmpty(list: List<MediaData>): Int {
        var index = -1
        run breaker@ {
            list.forEachIndexed { i, it ->
                if(it.caption?.trim().isNullOrEmpty()) {
                    index = i
                    return@breaker
                }
            }
        }
        return index
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

    private suspend fun changeSelectedMediaList(list: List<MediaData>){
        requestData.changeSelectedMedia(list)
        selectedMediaMutex.withLock {
            _selectedMediaList.value = list.toMutableList()
        }
    }

    suspend fun writeCapturedMedia(list: List<MediaData>) {
        requestData.changeCapturedMedia(list)
    }

    suspend fun clearCapturedMedia() {
        writeCapturedMedia(emptyList())
    }

    suspend fun clearMediaSelection() {
        selectedMedia.forEach {
            it.selected.value = false
        }
        selectedMedia.clear()
        changeSelectedMediaList(selectedMedia)
    }

    fun showMessage(msg: String) {
        _uiState.value = _uiState.value.copy(
            message = msg
        )
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(
            message = ""
        )
    }
}