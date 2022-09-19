package com.scogo.mediapicker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ComposeView
import com.scogo.mediapicker.compose.MediaPicker
import com.scogo.mediapicker.compose.onMediaPick

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        findViewById<ComposeView>(R.id.composeView).setContent {
            val state = remember { mutableStateOf("")}
            Button(
                onClick = {
                    MediaPicker.pick(
                        activity = this,
                        multiple = true,
                        captionMandatory = true,
                        callback = onMediaPick {
                            runOnUiThread {

                            }
                        }
                    )
                },
                content = {
                    Text(text = state.value)
                }
            )
        }
    }
}