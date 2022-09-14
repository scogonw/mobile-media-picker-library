package com.scogo.mediapicker.compose

import com.scogo.mediapicker.core.callback.MediaPickerCallback
import com.scogo.mediapicker.core.media.MediaData
import com.scogo.mediapicker.core.media.MediaPickerConfiguration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

object SharedDataHolder {

    private val clearLock = Mutex()

    private val pickerConfigLock = Mutex()
    private var pickerConfig = MediaPickerConfiguration()

    private val selectedMediaLock = Mutex()
    private var selectedMediaList = listOf<MediaData>()
    private val selectedMedia = MutableStateFlow<List<MediaData>>(emptyList())

    private val currentMediaCallbackLock = Mutex()
    private var currentMediaCallback: MediaPickerCallback? = null

    suspend fun writeMediaCallback(
        callback: MediaPickerCallback
    ){
        currentMediaCallbackLock.withLock {
            currentMediaCallback = callback
        }
    }

    fun triggerMediaCallback() {
        currentMediaCallback?.onPick(selectedMediaList)
    }

    fun readSelectedMedia() = selectedMediaList
    fun readPickerConfig() = pickerConfig

    suspend fun changeSelectedMedia(
        list: List<MediaData>
    ) {
        selectedMediaLock.withLock {
            selectedMediaList = list
            selectedMedia.value = list.toMutableList()
        }
    }

    suspend fun changePickerConfig(
        config: MediaPickerConfiguration
    ){
        pickerConfigLock.withLock {
            pickerConfig = config
        }
    }

    suspend fun clear() {
        clearLock.withLock {
            pickerConfig = MediaPickerConfiguration.EMPTY
            changeSelectedMedia(emptyList())
        }
    }
}