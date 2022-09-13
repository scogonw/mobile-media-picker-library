package com.scogo.mediapicker.common.ui.components

import android.widget.ImageView
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.scogo.mediapicker.common.ui_theme.ButtonDimes
import com.scogo.mediapicker.common.ui_theme.Dimens
import com.scogo.mediapicker.core.media.MediaData
import com.scogo.mediapicker.core.media.MimeTypes
import kotlinx.coroutines.launch

@Composable
fun MediaView(
    modifier: Modifier = Modifier,
    media: MediaData,
    onSelectMedia: suspend (MediaData) -> Boolean? = { null },
    onItemClick: (MediaData) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val selected = remember { mutableStateOf(media.selected) }
    val padding = animateDpAsState(
        targetValue = if (selected.value) Dimens.One else Dimens.Zero,
        animationSpec = spring(Spring.DampingRatioHighBouncy)
    )

    val isVideo = media.mimeType?.contains(MimeTypes.VIDEO.name) ?: false

    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopStart,
        content = {
            AndroidView(
                modifier = modifier
                    .padding(if(padding.value.value > 0) padding.value else Dimens.Zero)
                    .clickable {
                        onItemClick(media)
                        scope.launch {
                            onSelectMedia(media)?.let {
                                selected.value = it
                            }
                        }
                    },
                factory = {
                    ImageView(it).apply {
                        load(source = media.uri)
                        maxSize()
                    }
                }
            )
            if(selected.value) {
                CircleCheckbox(
                    modifier = Modifier.size(ButtonDimes.Five),
                    checked = selected.value,
                    enabled = false,
                )
            }
        }
    )
}