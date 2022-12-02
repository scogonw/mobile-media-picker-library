package com.scogo.mediapicker.compose.presentation.camera

import android.content.Context
import android.media.MediaActionSound
import android.net.Uri
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.PendingRecording
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.core.util.Consumer
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*
import java.util.concurrent.Executor
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

internal suspend fun Context.getCameraProvider(
): ProcessCameraProvider {
    return suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(this).also { provider ->
            provider.addListener(
                {
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
) {
    val outputFile = File(outputDirectory, UUID.randomUUID().toString() + ".jpeg")
    val outputOptions = ImageCapture.OutputFileOptions
        .Builder(outputFile)
        .build()

    val savedCallback = object : ImageCapture.OnImageSavedCallback {
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


internal suspend fun Context.bindCameraWithUsecase(
    cameraCaptureType: CameraCaptureType,
    lifecycleOwner: LifecycleOwner,
    cameraSelector: CameraSelector,
    imageCapture: ImageCapture,
    videoCapture: VideoCapture<Recorder>,
    preview: Preview,
    previewView: PreviewView,
    onStart: () -> Unit,
    onFinish: () -> Unit,
) {
    try {
        onStart.invoke()
        val context = this
        val captureComponent = when (cameraCaptureType) {
            is CameraCaptureType.Video -> videoCapture
            else -> imageCapture
        }
        val cameraProvider = context.getCameraProvider()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            captureComponent,
        )
        preview.setSurfaceProvider(previewView.surfaceProvider)
        onFinish.invoke()
    } catch (e: Exception) {
        e.printStackTrace()
        Log.e(e.message, e.localizedMessage.orEmpty())
    }
}

internal fun safeTakePhoto(
    cameraCaptureType: CameraCaptureType,
    changeCameraCaptureType: (CameraCaptureType) -> Unit,
    imageCapture: ImageCapture,
    outputDirectory: File,
    executor: Executor,
    onError: (Exception) -> Unit,
    mediaActionSound: MediaActionSound,
    onMediaCaptured: (Uri) -> Unit,
) {
    if (cameraCaptureType != CameraCaptureType.Image) {
        changeCameraCaptureType.invoke(CameraCaptureType.Image)
    }
    imageCapture.takePhoto(
        outputDirectory = outputDirectory,
        executor = executor,
        onImageSaved = {
            mediaActionSound.play(MediaActionSound.SHUTTER_CLICK)
            onMediaCaptured(it)
        },
        onError = onError,
    )
}

internal fun safeTakeVideo(
    cameraCaptureType: CameraCaptureType,
    changeCameraCaptureType: (CameraCaptureType) -> Unit,
    videoRecordingEnable: Boolean,
    isReleased: Boolean,
    oldRecordingSession: Recording?,
    recording: PendingRecording?,
    executor: Executor,
    listener: Consumer<VideoRecordEvent>,
    onRecordingSession: (Recording?) -> Unit,
    onRecording: (Boolean) -> Unit,
) {
    if (cameraCaptureType != CameraCaptureType.Video) {
        changeCameraCaptureType.invoke(CameraCaptureType.Video)
    }
    if (videoRecordingEnable && !isReleased && oldRecordingSession == null) {
        onRecordingSession.invoke(recording?.start(executor, listener))
    } else {
        onRecording.invoke(false)
        oldRecordingSession?.stop()
        onRecordingSession.invoke(null)
    }
}













