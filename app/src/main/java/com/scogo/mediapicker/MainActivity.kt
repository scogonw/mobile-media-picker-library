package com.scogo.mediapicker

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ComposeView
import com.scogo.mediapicker.compose.MediaPicker
import com.scogo.mediapicker.core.callback.MediaPickerCallback
import com.scogo.mediapicker.core.media.MediaData

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
                        callback = object : MediaPickerCallback {
                            override fun onPick(list: List<MediaData>) {
                                runOnUiThread {
                                    Log.e("MediaPicker","First Picked media size ${list.size}")
                                }
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