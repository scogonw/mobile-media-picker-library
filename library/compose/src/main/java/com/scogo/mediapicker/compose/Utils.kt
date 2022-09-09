package com.scogo.mediapicker.compose

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.scogo.mediapicker.compose.home.HomeActivity
import com.scogo.mediapicker.compose.media.MediaViewModel

@Composable
internal fun composeActivity(): Activity {
    val context = LocalContext.current
    return (context as Activity)
}

@Composable
internal fun activityMediaViewModel(): MediaViewModel {
    val context = LocalContext.current
    return (context as HomeActivity).mediaViewModel
}

@Composable
internal fun activitySharedViewModel(): SharedViewModel {
    val context = LocalContext.current
    return (context as HomeActivity).viewModel
}