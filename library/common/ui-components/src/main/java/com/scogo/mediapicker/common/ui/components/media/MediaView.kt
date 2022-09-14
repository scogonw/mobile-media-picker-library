package com.scogo.mediapicker.common.ui.components.media

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.scogo.mediapicker.common.ui.components.custom.CircleCheckbox
import com.scogo.mediapicker.common.ui.components.custom.CustomImageView
import com.scogo.mediapicker.common.ui.components.util.load
import com.scogo.mediapicker.common.ui_theme.ButtonDimes
import com.scogo.mediapicker.common.ui_theme.Dimens
import com.scogo.mediapicker.core.media.MediaData
import com.scogo.mediapicker.core.media.MimeTypes

@Composable
fun MediaView(
    modifier: Modifier = Modifier,
    media: MediaData,
    onItemClick: (MediaData) -> Unit,
) {
    val selected = media.selected.collectAsState()

    val padding = animateDpAsState(
        targetValue = if (selected.value) Dimens.One else Dimens.Zero,
        animationSpec = spring(Spring.DampingRatioHighBouncy)
    )

    val safePadding = if (padding.value.value > 0) {
        padding.value
    } else {
        Dimens.Zero
    }

    val isVideo = media.mimeType?.contains(MimeTypes.VIDEO.name) ?: false

    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopStart,
        content = {
            CustomImageView(
                modifier = modifier
                    .padding(safePadding)
                    .clickable {
                        onItemClick(media)
                    },
                content = {
                    it.load(source = media.uri)
                }
            )
            if(selected.value) {
                CircleCheckbox(
                    modifier = Modifier.size(ButtonDimes.Five),
                    checked = selected.value,
                    enabled = false,
                )
            }
            if(isVideo) {
                Icon(
                    modifier = Modifier
                        .padding(safePadding + Dimens.One)
                        .size(ButtonDimes.Five)
                        .align(Alignment.BottomStart)
                    ,
                    imageVector = Icons.Default.PlayCircle,
                    tint = Color.White,
                    contentDescription = null
                )
            }
        }
    )
}