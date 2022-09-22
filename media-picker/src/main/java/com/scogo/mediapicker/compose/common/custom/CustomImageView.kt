package com.scogo.mediapicker.compose.common.custom

import android.widget.ImageView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.scogo.mediapicker.compose.common.util.maxSize

@Composable
internal fun CustomImageView(
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