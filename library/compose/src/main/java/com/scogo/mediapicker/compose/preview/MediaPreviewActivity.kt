package com.scogo.mediapicker.compose.preview

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.scogo.mediapicker.common.ui_theme.ScogoTheme
import com.scogo.mediapicker.compose.SharedDataHolder
import com.scogo.mediapicker.compose.media.MediaViewModel
import com.scogo.mediapicker.compose.media.MediaViewModelFactory
import com.scogo.mediapicker.core.data.impl.MediaRepositoryImpl

internal class MediaPreviewActivity: ComponentActivity() {

    companion object {
        fun start(from: Activity) {
            val i = Intent(from, MediaPreviewActivity::class.java)
            from.startActivity(i)
        }
    }

    val mediaViewModel: MediaViewModel by viewModels {
        MediaViewModelFactory(
            repo = MediaRepositoryImpl(this),
            config = SharedDataHolder.readPickerConfig()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mediaViewModel.changeSelectedMediaList(
            list = SharedDataHolder.readSelectedMedia()
        )
        setContent {
            ScogoTheme {
                MediaPreviewScreen()
            }
        }
    }
}