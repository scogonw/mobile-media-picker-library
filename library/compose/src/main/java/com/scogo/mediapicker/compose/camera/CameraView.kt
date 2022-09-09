package com.scogo.mediapicker.compose.camera

import android.media.MediaActionSound
import android.net.Uri
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.scogo.mediapicker.compose.components.CameraActionIcon
import java.io.File
import java.util.concurrent.Executor

@Composable
internal fun CameraView(
    modifier: Modifier = Modifier,
    outputDirectory: File,
    executor: Executor,
    mediaActionSound: MediaActionSound,
    footerContent: @Composable () -> Unit,
    onImageCaptured: (Uri) -> Unit,
    onError: (Exception) -> Unit
){
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

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

    LaunchedEffect(lensFacing.value, flashModeAuto.value) {
        val cameraProvider = context.getCameraProvider()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            imageCapture
        )
        preview.setSurfaceProvider(previewView.surfaceProvider)
    }

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier.fillMaxSize()
    ) {
        AndroidView(
            factory = {
                previewView
            },
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            footerContent()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 35.dp, vertical = 20.dp),
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
                            contentDescription = "flash",
                            modifier = Modifier
                                .size(32.dp)
                                .padding(4.dp),
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
                                onImageCaptured(it)
                            },
                            onError = onError,
                        )
                    },
                    content = {
                        Icon(
                            imageVector = Icons.Sharp.Lens,
                            contentDescription = "Take picture",
                            modifier = Modifier
                                .size(75.dp)
                                .padding(1.dp)
                                .border(2.dp, Color.White, CircleShape),
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
                            contentDescription = "switch camera",
                            modifier = Modifier
                                .size(32.dp)
                                .padding(4.dp),
                            tint = Color.White
                        )
                    }
                )
            }
        }
    }
}