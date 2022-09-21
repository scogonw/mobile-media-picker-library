package com.scogo.mediapicker.common.ui.components.media

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.StyledPlayerView

@Composable
fun VideoView(
    modifier: Modifier,
    uri: Uri?,
){
    val context = LocalContext.current
    val exoPlayer = remember(context) {
        ExoPlayer.Builder(context).build().also {
            val mediaItem = MediaItem.Builder()
                .setUri(uri)
                .build()

            it.setMediaItem(mediaItem)
            it.playWhenReady = false
            it.prepare()
        }
    }
    Box(
        modifier = modifier,
        content = {
            DisposableEffect(AndroidView(
                modifier = modifier,
                factory = {
                    StyledPlayerView(it).also { view ->
                        with(view) {
                            showController()
                            player = exoPlayer
                        }
                    }
                }
            )) {
                onDispose {
                    exoPlayer.release()
                }
            }
        }
    )
}