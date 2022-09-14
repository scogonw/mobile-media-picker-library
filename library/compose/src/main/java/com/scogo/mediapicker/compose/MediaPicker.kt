package com.scogo.mediapicker.compose

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.scogo.mediapicker.compose.home.HomeActivity
import com.scogo.mediapicker.core.media.MediaPickerConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MediaPicker private constructor(
    private var mActivity: Activity
) {
    private val scope = CoroutineScope(Dispatchers.Main)

    private fun initDataHolder(
        config: MediaPickerConfiguration,
    ) {
        scope.launch {
            with(SharedDataHolder) {
                clear()
                changePickerConfig(config)
            }
        }
    }

    companion object {
        @Throws
        fun pick(
            activity: Activity,
            multiple: Boolean = false,
            onImageSelected: (List<Uri>) -> Unit
        ) {
            val picker = MediaPicker(
                mActivity = activity
            )
            with(picker) {
                initDataHolder(
                    config = MediaPickerConfiguration(
                        multipleAllowed = multiple
                    )
                )
                val intent = Intent(
                    mActivity, HomeActivity::class.java
                ).apply {
                    flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                }
                mActivity.startActivity(intent)
            }
        }
    }
}