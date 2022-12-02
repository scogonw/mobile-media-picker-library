package com.scogo.mediapicker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.platform.ComposeView
import com.scogo.mediapicker.compose.core.media.MimeTypes
import com.scogo.mediapicker.compose.scogoMediaPick

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        findViewById<ComposeView>(R.id.composeView).setContent {
            Button(
                onClick = {
                    scogoMediaPick(
                        multiple = true,
                        captionMandatory = true
                    ) {

                    }
                },
                content = {
                    Text(text = "Pick Images/Videos")
                }
            )
        }
    }
}