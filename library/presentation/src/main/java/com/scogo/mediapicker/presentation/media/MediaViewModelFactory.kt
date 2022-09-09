package com.scogo.mediapicker.presentation.media

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.scogo.mediapicker.core.data.api.MediaRepository
import com.scogo.mediapicker.core.media.MediaPickerConfiguration

@Suppress("UNCHECKED_CAST")
class MediaViewModelFactory(
    private val repo: MediaRepository,
    private val config: MediaPickerConfiguration
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(MediaViewModel::class.java)) {
            MediaViewModel(repo, config) as T
        }else {
            throw IllegalArgumentException("")
        }
    }
}