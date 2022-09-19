package com.scogo.mediapicker.compose.preview

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.scogo.mediapicker.common.ui_theme.ScogoTheme
import com.scogo.mediapicker.compose.media.MediaViewModel
import com.scogo.mediapicker.compose.media.MediaViewModelFactory
import com.scogo.mediapicker.core.data.impl.MediaRepositoryImpl
import com.scogo.mediapicker.core.event.PushEvent
import com.scogo.mediapicker.utils.Consts.WORK_ID
import org.greenrobot.eventbus.EventBus

internal class MediaPreviewActivity: ComponentActivity() {

    companion object {
        fun start(from: Activity, workId: String) {
            val intent = Intent(from, MediaPreviewActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                putExtra(WORK_ID,workId)
            }
            from.startActivity(intent)
        }
    }

    val mediaViewModel: MediaViewModel by viewModels {
        MediaViewModelFactory(MediaRepositoryImpl(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val workId = intent.extras?.getString(WORK_ID)
        if(!mediaViewModel.initRequestData(workId)) finish()
        else mediaViewModel.syncSelectedMediaList()

        setContent {
            ScogoTheme {
                MediaPreviewScreen(
                    onMediaPicked = {
                        mediaViewModel.readRequestData().mediaPicked()
                        EventBus.getDefault().postSticky(PushEvent(
                            workId = mediaViewModel.readRequestData().readId()
                        ))
                        finish()
                    },
                    onBack = {
                        finish()
                    }
                )
            }
        }
    }
}