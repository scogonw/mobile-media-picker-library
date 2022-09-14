package com.scogo.mediapicker.core.request

import com.scogo.mediapicker.core.callback.MediaPickerCallback
import com.scogo.mediapicker.core.media.MediaData
import com.scogo.mediapicker.core.media.MediaPickerConfiguration
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class PickerRequestData private constructor(
    private var id: String,
    private var config: MediaPickerConfiguration,
    private var callback: MediaPickerCallback
) {
    companion object {
        fun create(
            id: String,
            config: MediaPickerConfiguration,
            callback: MediaPickerCallback
        ): PickerRequestData {
            return PickerRequestData(
                id = id,
                config = config,
                callback = callback
            )
        }
    }

    private val selectedMediaLock = Mutex()
    private var selectedMediaList = listOf<MediaData>()

    fun mediaPicked() = callback.onPick(selectedMediaList)

    fun readId() = id
    fun readPickerConfig() = config
    fun readSelectedMedia() = selectedMediaList

    suspend fun changeSelectedMedia(list: List<MediaData>) {
        selectedMediaLock.withLock {
            selectedMediaList = list
        }
    }
}