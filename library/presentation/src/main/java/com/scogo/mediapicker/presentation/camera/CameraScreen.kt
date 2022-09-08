package com.scogo.mediapicker.presentation.camera

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.scogo.mediapicker.compose.camera.CameraView
import java.util.concurrent.Executor

@Composable
internal fun CameraScreen(
    holder: CameraDataHolder,
    executor: Executor
) {
    CameraScreen(
        holder = holder,
        executor = executor,
        onImageCaptured = {

        }, onError = {

        }
    )
}

@Composable
private fun CameraScreen(
    holder: CameraDataHolder,
    executor: Executor,
    onImageCaptured: (Uri) -> Unit,
    onError: (Exception) -> Unit
) {
    CameraView(
        outputDirectory = holder.outputDirectory,
        executor = executor,
        mediaActionSound = holder.sound,
        footerContent = {
            MediaHorizontalList()
        },
        onImageCaptured = onImageCaptured,
        onError = onError
    )
}

@Composable
internal fun MediaHorizontalList(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState()
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        state = state,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {

    }
}

@Preview
@Composable
private fun CameraScreenPreview() {
    MediaHorizontalList()
}