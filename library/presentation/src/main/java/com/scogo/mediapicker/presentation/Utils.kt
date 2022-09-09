package com.scogo.mediapicker.presentation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.scogo.mediapicker.presentation.home.HomeActivity
import com.scogo.mediapicker.presentation.media.MediaViewModel

@Composable
fun composeActivity(): Activity {
    val context = LocalContext.current
    return (context as Activity)
}

@Composable
fun activityMediaViewModel(): MediaViewModel {
    val context = LocalContext.current
    return (context as HomeActivity).mediaViewModel
}

@Composable
fun activitySharedViewModel(): SharedViewModel {
    val context = LocalContext.current
    return (context as HomeActivity).viewModel
}