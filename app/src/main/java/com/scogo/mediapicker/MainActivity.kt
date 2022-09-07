package com.scogo.mediapicker

import android.media.MediaActionSound
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.ComposeView
import com.scogo.mediapicker.compose.camera.CameraView
import com.scogo.mediapicker.utils.getMediaDirectory
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