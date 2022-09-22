package com.scogo.mediapicker.compose

import android.app.Activity
import android.content.Intent
import com.scogo.mediapicker.compose.core.callback.MediaPickerCallback
import com.scogo.mediapicker.compose.core.media.MediaData
import com.scogo.mediapicker.compose.core.media.MediaPickerConfiguration
import com.scogo.mediapicker.compose.core.request.PickerRequestWorker
import com.scogo.mediapicker.compose.presentation.home.HomeActivity
import com.scogo.mediapicker.compose.util.Consts.WORK_ID

class MediaPicker private constructor() {
    companion object {
        @Throws
        fun pick(
            activity: Activity,
            multiple: Boolean = false,
            captionMandatory: Boolean = false,
            callback: MediaPickerCallback,
        ) {
            val worker = PickerRequestWorker.getInstance()
            val request = worker.enqueue(
                config = MediaPickerConfiguration(
                    multipleAllowed = multiple,
                    captionMandatory = captionMandatory
                ),
                callback = callback
            )
            val intent = Intent(activity, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            intent.putExtra(WORK_ID,request)
            activity.startActivity(intent)
        }
    }
}

fun Activity.scogoMediaPick(
    multiple: Boolean = false,
    captionMandatory: Boolean = false,
    onPick: (List<MediaData>) -> Unit
){
    MediaPicker.pick(
        activity = this,
        multiple = multiple,
        captionMandatory = captionMandatory,
        callback = object : MediaPickerCallback {
            override fun onPick(list: List<MediaData>) {
                onPick.invoke(list)
            }
        }
    )
}