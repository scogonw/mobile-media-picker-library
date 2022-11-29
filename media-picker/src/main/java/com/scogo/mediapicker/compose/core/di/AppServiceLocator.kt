package com.scogo.mediapicker.compose.core.di

import android.media.MediaActionSound
import java.util.concurrent.Executor
import java.util.concurrent.Executors

internal object AppServiceLocator {
    val mediaSound by lazy {
        MediaActionSound()
    }
    val executor: Executor by lazy {
        Executors.newSingleThreadExecutor()
    }
}