package com.scogo.mediapicker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.compose.ui.platform.ComposeView
import com.scogo.mediapicker.compose.CameraView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<ComposeView>(R.id.composeView).setContent {
            CameraView(onImageCaptured = {

            }, onError = {

            })
        }
    }
}