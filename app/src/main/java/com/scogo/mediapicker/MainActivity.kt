package com.scogo.mediapicker

import android.media.MediaActionSound
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.ComposeView
import com.scogo.mediapicker.compose.camera.CameraView
import com.scogo.mediapicker.utils.getMediaDirectory
import java.io.File
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        val executor = Executors.newSingleThreadExecutor()

        findViewById<ComposeView>(R.id.composeView).setContent {
            CameraView(
                outputDirectory = getMediaDirectory("scogo"),
                executor = executor ,
                mediaActionSound = MediaActionSound(),
                onImageCaptured = {},
                onError = {}
            )
        }
    }
}