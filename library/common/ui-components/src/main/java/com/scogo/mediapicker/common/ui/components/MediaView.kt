package com.scogo.mediapicker.common.ui.components

import android.net.Uri
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

@Composable
fun MediaView(
    modifier: Modifier = Modifier,
    uri: Uri,
    isSelected: Boolean,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomEnd,
        content = {
            Image(
                modifier = modifier,
                painter = rememberAsyncImagePainter(uri),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
            if(isSelected) {
                CircleCheckbox(
                    checked = isSelected,
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
        uri = Uri.EMPTY,
        isSelected = true
    )
}