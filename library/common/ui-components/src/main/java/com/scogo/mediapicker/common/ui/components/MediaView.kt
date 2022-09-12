package com.scogo.mediapicker.common.ui.components

import android.widget.ImageView
import android.widget.LinearLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.scogo.mediapicker.core.media.MediaData
import com.scogo.mediapicker.core.media.MimeTypes

@Composable
fun MediaView(
    modifier: Modifier = Modifier,
    media: MediaData,
) {
    val isVideo = media.mimeType?.contains(MimeTypes.VIDEO.name) ?: false
    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomEnd,
        content = {
            AndroidView(
                modifier = modifier,
                factory = {
                    ImageView(it).load(
                        source = media.uri
                    ).apply {
                        val params = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT
                        )
                        layoutParams = params
                    }
                }
            )
            if(media.selected) {
                CircleCheckbox(
                    checked = media.selected,
                    enabled = false,
                )
            }
        }
    )
}

@Composable
@Preview
private fun MediaPreview() {
    MediaView(
        modifier = Modifier.size(100.dp),
        media = MediaData.EMPTY
    )
}