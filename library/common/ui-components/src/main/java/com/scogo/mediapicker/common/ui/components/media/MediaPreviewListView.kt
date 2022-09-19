package com.scogo.mediapicker.common.ui.components.media

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.scogo.mediapicker.common.ui.components.custom.CustomImageView
import com.scogo.mediapicker.common.ui.components.util.load
import com.scogo.mediapicker.core.media.MediaData
import com.scogo.mediapicker.core.media.MimeTypes

@Composable
fun MediaPreviewListView(
    modifier: Modifier,
    viewModifier: Modifier,
    media: MediaData,
) {
    val isVideo = media.mimeType?.contains(MimeTypes.VIDEO.name) ?: false

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
        content = {
            if(isVideo) {
                VideoView(
                    modifier = viewModifier,
                    uri = media.uri
                )
            }else {
                CustomImageView(
                    modifier = viewModifier,
                    content = {
                        it.load(source = media.uri)
                    }
                )
            }
        }
    )
}
