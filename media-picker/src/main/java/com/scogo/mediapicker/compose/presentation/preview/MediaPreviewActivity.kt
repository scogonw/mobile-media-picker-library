package com.scogo.mediapicker.compose.presentation.preview

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
import com.scogo.mediapicker.compose.core.data.impl.MediaRepositoryImpl
import com.scogo.mediapicker.compose.core.event.PushEvent
import com.scogo.mediapicker.compose.presentation.media.MediaViewModel
import com.scogo.mediapicker.compose.presentation.media.MediaViewModelFactory
import com.scogo.mediapicker.compose.presentation.theme.ScogoTheme
import com.scogo.mediapicker.compose.util.Consts.MEDIA_INDEX
import com.scogo.mediapicker.compose.util.Consts.WORK_ID
import com.scogo.mediapicker.compose.util.FileUtil
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus

internal class MediaPreviewActivity: ComponentActivity() {

	companion object {
		fun start(
			from: Activity,
			workId: String,
			index: Int = 0,
		) {
			val intent = Intent(from, MediaPreviewActivity::class.java).apply {
				addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
				putExtra(WORK_ID,workId)
				putExtra(MEDIA_INDEX,index)
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

		val index = intent.extras?.getInt(MEDIA_INDEX) ?: 0
		mediaViewModel.mediaIndex = index

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
						EventBus.getDefault().postSticky(
							PushEvent(
							workId = mediaViewModel.readRequestData().readId()
						)
						)
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
						media.uri = withContext(Dispatchers.IO) {
                            FileUtil.saveImage(this@MediaPreviewActivity,newUri)
                        }
						val index = mediaViewModel.updateMedia(media)
						val workId = mediaViewModel.readRequestData().readId()
						finish()
						start(
							from = this@MediaPreviewActivity,
							workId = workId,
							index = index
						)
					}
				}
			}
		}
	}

}