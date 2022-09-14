package com.scogo.mediapicker.compose

import android.app.Activity
import android.content.Intent
import com.scogo.mediapicker.compose.home.HomeActivity
import com.scogo.mediapicker.core.callback.MediaPickerCallback
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
        callback: MediaPickerCallback
    ) {
        scope.launch {
            with(SharedDataHolder) {
                clear()
                changePickerConfig(config)
                writeMediaCallback(callback)
            }
        }
    }

    companion object {
        @Throws
        fun pick(
            activity: Activity,
            multiple: Boolean = false,
            callback: MediaPickerCallback,
        ) {
            val picker = MediaPicker(
                mActivity = activity
            )
            with(picker) {
                initDataHolder(
                    config = MediaPickerConfiguration(multiple),
                    callback = callback
                )
                val intent = Intent(
                    activity, HomeActivity::class.java
                ).apply {
                    flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                }
                activity.startActivity(intent)
            }
        }
    }
}