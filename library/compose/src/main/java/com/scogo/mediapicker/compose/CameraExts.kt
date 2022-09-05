package com.scogo.mediapicker.compose

import android.content.Context
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

internal suspend fun Context.getCameraProvider(
): ProcessCameraProvider {
    return suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(this).also { provider ->
            provider.addListener({
                    continuation.resume(provider.get())
                }, ContextCompat.getMainExecutor(this)
            )
        }
    }
}