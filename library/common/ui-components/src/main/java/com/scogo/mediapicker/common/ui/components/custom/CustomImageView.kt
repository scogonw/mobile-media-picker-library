package com.scogo.mediapicker.common.ui.components.custom

import android.widget.ImageView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.scogo.mediapicker.common.ui.components.util.maxSize

@Composable
fun CustomImageView(
    modifier: Modifier,
    content: (ImageView) -> Unit
) {
    AndroidView(
        modifier = modifier,
        factory = {
            ImageView(it).apply {
                maxSize()
                content(this)
            }
        }
    )
}