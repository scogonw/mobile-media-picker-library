package com.scogo.mediapicker.presentation.picker

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.scogo.mediapicker.core.media.MediaPickerConfiguration
import com.scogo.mediapicker.presentation.home.HomeActivity

class MediaPicker private constructor(
    private var configuration: MediaPickerConfiguration? = null,
    private var activity: Activity? = null
): DefaultLifecycleObserver {

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
            val intent = Intent(it,HomeActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            it.startActivity(intent)
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        configuration = null
        activity = null
        super.onDestroy(owner)
    }
}