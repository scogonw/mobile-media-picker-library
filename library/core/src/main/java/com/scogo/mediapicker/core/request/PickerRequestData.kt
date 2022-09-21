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

    fun mediaPicked() = callback.onPick(selectedMediaList)

    private val selectedMediaLock = Mutex()
    private var selectedMediaList = listOf<MediaData>()

    private val capturedMediaLock = Mutex()
    private var capturedMedia: List<MediaData> = mutableListOf()

    fun readId() = id
    fun readPickerConfig() = config
    fun readSelectedMedia() = selectedMediaList
    fun readCapturedMedia() = capturedMedia

    suspend fun changeSelectedMedia(list: List<MediaData>) {
        selectedMediaLock.withLock {
            selectedMediaList = list
        }
    }

    suspend fun changeCapturedMedia(list: List<MediaData>) {
        capturedMediaLock.withLock {
            capturedMedia = list
        }
    }

}