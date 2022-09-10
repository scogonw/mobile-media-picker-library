package com.scogo.mediapicker.common.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
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
            Image(
                modifier = modifier,
                painter = rememberAsyncImagePainter(
                    model = if(isVideo) {
                        loadThumbnail(uri = media.uri).value
                    }else {
                        media.uri
                    }
                ),
                contentScale = ContentScale.Crop,
                contentDescription = null
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