package com.scogo.mediapicker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Text
import androidx.compose.ui.platform.ComposeView
import com.scogo.mediapicker.presentation.picker.MediaPicker

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        val picker = MediaPicker.getInstance(
            activity = this
        )

        findViewById<ComposeView>(R.id.composeView).setContent {
            Text(text = "Hello")
            picker.pick {

            }
        }
    }
}