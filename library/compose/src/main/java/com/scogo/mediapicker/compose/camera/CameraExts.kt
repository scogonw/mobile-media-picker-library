package com.scogo.mediapicker.compose.camera

import android.content.Context
import android.net.Uri
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import java.io.File
import java.util.*
import java.util.concurrent.Executor
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
internal fun ImageCapture.takePhoto(
    outputDirectory: File,
    executor: Executor,
    onImageSaved: (Uri) -> Unit,
    onError: (Exception) -> Unit,
){
    val outputFile = File(outputDirectory,UUID.randomUUID().toString()+".jpg")
    val outputOptions = ImageCapture.OutputFileOptions
        .Builder(outputFile)
        .build()

    val savedCallback = object: ImageCapture.OnImageSavedCallback {
        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
            val uri = Uri.fromFile(outputFile)
            onImageSaved(uri)
        }
        override fun onError(exception: ImageCaptureException) {
            onError(exception)
        }
    }
    takePicture(outputOptions, executor, savedCallback)
}
