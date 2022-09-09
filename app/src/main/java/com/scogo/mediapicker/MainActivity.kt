package com.scogo.mediapicker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.platform.ComposeView
import com.scogo.mediapicker.compose.MediaPicker

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        val picker = MediaPicker.getInstance(
            activity = this
        )

        findViewById<ComposeView>(R.id.composeView).setContent {
            Button(
                onClick = {
                    picker.pick {

                    }
                },
                content = {
                    Text(text = "Pick Media")
                }
            )
        }
    }
}