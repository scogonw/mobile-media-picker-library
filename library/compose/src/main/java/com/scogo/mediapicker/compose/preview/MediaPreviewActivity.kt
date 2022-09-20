package com.scogo.mediapicker.compose.preview

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import com.scogo.mediapicker.common.ui_theme.ScogoTheme
import com.scogo.mediapicker.compose.media.MediaViewModel
import com.scogo.mediapicker.compose.media.MediaViewModelFactory
import com.scogo.mediapicker.core.data.impl.MediaRepositoryImpl
import com.scogo.mediapicker.core.event.PushEvent
import com.scogo.mediapicker.utils.Consts.WORK_ID
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

    private val scope = CoroutineScope(Dispatchers.Main)
    private var activityResultLauncher: ActivityResultLauncher<Uri?>? = null

    private val cropImageResultContract = object: ActivityResultContract<Uri?, Uri?>() {
        override fun createIntent(context: Context, input: Uri?): Intent {
            return CropImage.activity(input).getIntent(this@MediaPreviewActivity)
        }
        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            return CropImage.getActivityResult(intent)?.uri
        }
    }

    val mediaViewModel: MediaViewModel by viewModels {
        MediaViewModelFactory(MediaRepositoryImpl(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val workId = intent.extras?.getString(WORK_ID)
        if(!mediaViewModel.initRequestData(workId)) finish()
        else mediaViewModel.syncSelectedMediaList()

        registerLauncher()

        setContent {
            ScogoTheme {
                MediaPreviewScreen(
                    cropImage = { media ->
                        media.uri?.let {
                            mediaViewModel.cropMedia = media
                            cropImage(it)
                        }
                    },
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

    private fun cropImage(uri: Uri) {
        activityResultLauncher?.launch(uri)
    }

    private fun registerLauncher() {
        activityResultLauncher = registerForActivityResult(cropImageResultContract) {
            it?.let { newUri ->
                scope.launch {
                    mediaViewModel.cropMedia?.also { media ->
                        media.uri = newUri
                        mediaViewModel.updateMedia(media)
                    }
                }
            }
        }
    }

}