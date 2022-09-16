package com.scogo.mediapicker.compose.camera

import android.annotation.SuppressLint
import android.content.ContentValues
import android.media.MediaActionSound
import android.net.Uri
import android.provider.MediaStore
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.video.*
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Cameraswitch
import androidx.compose.material.icons.sharp.FlashAuto
import androidx.compose.material.icons.sharp.FlashOn
import androidx.compose.material.icons.sharp.Lens
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.util.Consumer
import com.scogo.mediapicker.common.ui.components.camera.CameraActionIcon
import com.scogo.mediapicker.common.ui.components.custom.Chip
import com.scogo.mediapicker.common.ui_theme.ButtonDimes
import com.scogo.mediapicker.common.ui_theme.Dimens
import com.scogo.mediapicker.compose.util.ComposeTimer
import com.scogo.mediapicker.core.media.MimeTypes
import java.io.File
import java.util.*
import java.util.concurrent.Executor

@SuppressLint("MissingPermission")
@Composable
internal fun CameraView(
    modifier: Modifier = Modifier,
    outputDirectory: File,
    executor: Executor,
    mediaActionSound: MediaActionSound,
    footerContent: @Composable () -> Unit,
    bottomContent: @Composable () -> Unit = {},
    onMediaCaptured: (Uri) -> Unit,
    onError: (Exception) -> Unit
){
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val timer = ComposeTimer.get()
    val timerState = timer.readTime().collectAsState()
    val isRecording = remember { mutableStateOf(false) }

    LaunchedEffect(isRecording) {
        if(isRecording.value) {
            timer.start()
        }else {
            timer.stop()
        }
    }

    val lensFacing = rememberSaveable {
        mutableStateOf(CameraSelector.LENS_FACING_BACK)
    }
    val flashModeAuto = rememberSaveable {
        mutableStateOf(ImageCapture.FLASH_MODE_AUTO)
    }

    val preview = Preview.Builder().build()
    val previewView = remember {
        PreviewView(context)
    }

    val imageCapture =  ImageCapture.Builder()
        .setFlashMode(flashModeAuto.value)
        .build()

    val cameraSelector = CameraSelector.Builder()
        .requireLensFacing(lensFacing.value)
        .build()

    val qualitySelector = QualitySelector.from(
        Quality.UHD,
        FallbackStrategy.higherQualityOrLowerThan(Quality.SD)
    )

    val videoRecorder = Recorder.Builder()
        .setExecutor(executor)
        .setQualitySelector(qualitySelector)
        .build()

    val videoCapture = VideoCapture.withOutput(videoRecorder)

    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, UUID.randomUUID().toString())
        put(MediaStore.MediaColumns.MIME_TYPE, MimeTypes.VIDEO_MP4.name)
    }

    val mediaStoreOutputOptions = MediaStoreOutputOptions
        .Builder(context.contentResolver, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        .setContentValues(contentValues)
        .build()

    val recording = videoCapture.output
        .prepareRecording(context,mediaStoreOutputOptions)
        .withAudioEnabled()

    var recordingSession: Recording? = null

    val videoRecordingListener = Consumer<VideoRecordEvent> { event ->
        when(event) {
            is VideoRecordEvent.Start -> {
                isRecording.value = true
            }
            is VideoRecordEvent.Finalize -> {
                isRecording.value = false
                if(!event.hasError()) {
                    val uri = event.outputResults.outputUri
                    onMediaCaptured(uri)
                }else {
                    recordingSession?.close()
                    recordingSession = null
                }
            }
        }
    }

    LaunchedEffect(lensFacing.value, flashModeAuto.value) {
        val cameraProvider = context.getCameraProvider()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            imageCapture,
            videoCapture
        )
        preview.setSurfaceProvider(previewView.surfaceProvider)
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        AndroidView(
            factory = {
                previewView
            },
            modifier = Modifier.fillMaxSize()
        )
        if(isRecording.value) {
            Chip(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.Two)
                ,
                text = timerState.value
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            footerContent()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.Four, vertical = Dimens.Three),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CameraActionIcon(
                    modifier = Modifier.background(
                        color = Color.Gray.copy(alpha = 0.4f),
                        shape = CircleShape
                    ),
                    onClick = {
                        flashModeAuto.value = if(flashModeAuto.value == ImageCapture.FLASH_MODE_AUTO) {
                            ImageCapture.FLASH_MODE_ON
                        }else {
                            ImageCapture.FLASH_MODE_AUTO
                        }
                    },
                    content = {
                        Icon(
                            imageVector = if(flashModeAuto.value == ImageCapture.FLASH_MODE_AUTO){
                                Icons.Sharp.FlashAuto
                            }else {
                                Icons.Sharp.FlashOn
                            },
                            contentDescription = null,
                            modifier = Modifier
                                .size(Dimens.Four)
                                .padding(ButtonDimes.One),
                            tint = Color.White
                        )
                    }
                )
                CameraActionIcon(
                    onClick = {
                        imageCapture.takePhoto(
                            outputDirectory = outputDirectory,
                            executor = executor,
                            onImageSaved = {
                                mediaActionSound.play(MediaActionSound.SHUTTER_CLICK)
                                onMediaCaptured(it)
                            },
                            onError = onError,
                        )
                    },
                    onHold = { released ->
                        recordingSession = if (!released && recordingSession == null) {
                            recording.start(executor, videoRecordingListener)
                        } else {
                            isRecording.value = false
                            recordingSession?.stop()
                            null
                        }
                    },
                    content = {
                        Icon(
                            imageVector = Icons.Sharp.Lens,
                            contentDescription = null,
                            modifier = Modifier
                                .size(Dimens.Nine)
                                .padding(Dimens.HalfQuarter)
                                .border(Dimens.Quarter, Color.White, CircleShape),
                            tint = Color.White
                        )
                    },
                )
                CameraActionIcon(
                    modifier = Modifier.background(
                        color = Color.Gray.copy(alpha = 0.4f),
                        shape = CircleShape
                    ),
                    onClick = {
                        lensFacing.value = if(lensFacing.value == CameraSelector.LENS_FACING_BACK) {
                            CameraSelector.LENS_FACING_FRONT
                        } else {
                            CameraSelector.LENS_FACING_BACK
                        }
                    },
                    content = {
                        Icon(
                            imageVector = Icons.Sharp.Cameraswitch,
                            contentDescription = null,
                            modifier = Modifier
                                .size(Dimens.Four)
                                .padding(ButtonDimes.One),
                            tint = Color.White
                        )
                    }
                )
            }

            bottomContent()
        }
    }
}