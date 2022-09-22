package com.scogo.mediapicker.compose.presentation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.scogo.mediapicker.compose.presentation.home.HomeActivity
import com.scogo.mediapicker.compose.presentation.media.MediaViewModel
import com.scogo.mediapicker.compose.presentation.preview.MediaPreviewActivity

@Composable
internal fun composeActivity(): Activity {
    val context = LocalContext.current
    return (context as Activity)
}

@Composable
internal fun activityMediaViewModel(): MediaViewModel {
    val context = LocalContext.current
    val homeActivity = context as? HomeActivity
    val previewActivity = context as? MediaPreviewActivity
    return homeActivity?.mediaViewModel ?: previewActivity!!.mediaViewModel
}
