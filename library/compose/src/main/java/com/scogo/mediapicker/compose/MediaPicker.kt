package com.scogo.mediapicker.compose

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.scogo.mediapicker.compose.home.HomeActivity
import com.scogo.mediapicker.core.media.MediaPickerConfiguration

class MediaPicker private constructor(
    private var configuration: MediaPickerConfiguration? = null,
    private var activity: Activity? = null
) {
    companion object {
        fun getInstance(
            activity: Activity,
            configuration: MediaPickerConfiguration = MediaPickerConfiguration()
        ): MediaPicker {
            return MediaPicker(
                configuration = configuration,
                activity = activity
            )
        }
    }

    @Throws
    fun pick(multiple: Boolean = configuration?.multipleAllowed ?: true,
             onImageSelected: (List<Uri>) -> Unit
    ) {
        activity?.let {
            val intent = Intent(it, HomeActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            it.startActivity(intent)
        }
    }
}