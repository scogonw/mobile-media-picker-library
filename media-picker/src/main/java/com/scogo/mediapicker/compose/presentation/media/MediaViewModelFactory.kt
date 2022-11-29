package com.scogo.mediapicker.compose.presentation.media

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.scogo.mediapicker.compose.core.data.api.MediaRepository

@Suppress("UNCHECKED_CAST")
internal class MediaViewModelFactory(
    private val repo: MediaRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(MediaViewModel::class.java)) {
            MediaViewModel(repo) as T
        }else {
            throw IllegalArgumentException("")
        }
    }
}