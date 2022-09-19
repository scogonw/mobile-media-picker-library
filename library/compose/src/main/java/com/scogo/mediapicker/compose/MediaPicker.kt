package com.scogo.mediapicker.compose

import android.app.Activity
import android.content.Intent
import com.scogo.mediapicker.compose.home.HomeActivity
import com.scogo.mediapicker.core.callback.MediaPickerCallback
import com.scogo.mediapicker.core.media.MediaData
import com.scogo.mediapicker.core.media.MediaPickerConfiguration
import com.scogo.mediapicker.core.request.PickerRequestWorker
import com.scogo.mediapicker.utils.Consts.WORK_ID

class MediaPicker private constructor() {
    companion object {
        @Throws
        fun pick(
            activity: Activity,
            multiple: Boolean = false,
            callback: MediaPickerCallback,
        ) {
            val worker = PickerRequestWorker.getInstance()
            val request = worker.enqueue(
                config = MediaPickerConfiguration(multiple),
                callback = callback
            )
            val intent = Intent(activity, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            intent.putExtra(WORK_ID,request)
            activity.startActivity(intent)
        }
    }
}

/**
 * invoke as callback to requested state
 */
fun onMediaPick(pick: (List<MediaData>) -> Unit) = object : MediaPickerCallback {
    override fun onPick(list: List<MediaData>) {
        pick.invoke(list)
    }
}