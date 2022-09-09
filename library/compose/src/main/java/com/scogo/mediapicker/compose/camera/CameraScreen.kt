package com.scogo.mediapicker.compose.camera

import android.media.MediaActionSound
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.scogo.mediapicker.common.ui_res.R
import com.scogo.mediapicker.common.ui_theme.Dimens
import com.scogo.mediapicker.compose.activityMediaViewModel
import com.scogo.mediapicker.compose.components.MediaView
import com.scogo.mediapicker.compose.composeActivity
import com.scogo.mediapicker.compose.media.MediaViewModel
import com.scogo.mediapicker.core.di.AppServiceLocator
import com.scogo.mediapicker.core.media.MediaData
import java.io.File
import java.util.concurrent.Executor

@Composable
internal fun CameraScreen(
    outputDir: File,
    navigateToMedia: () -> Unit,
) {
    val activity = composeActivity()
    CameraScreen(
        mediaViewModel = activityMediaViewModel(),
        sound = AppServiceLocator.mediaSound,
        outputDir = outputDir,
        executor = AppServiceLocator.executor,
        onImageCaptured = {

        },
        onError = {

        },
        navigateToMedia = navigateToMedia,
        onBackPress = {
            activity.finish()
        }
    )
}

@Composable
private fun CameraScreen(
    mediaViewModel: MediaViewModel,
    sound: MediaActionSound,
    outputDir: File,
    executor: Executor,
    onImageCaptured: (Uri) -> Unit,
    onError: (Exception) -> Unit,
    navigateToMedia: () -> Unit,
    onBackPress: () -> Unit,
) {
    val mediaList = mediaViewModel.getMediaList().collectAsLazyPagingItems()
    BackHandler(onBack = onBackPress)

    CameraView(
        outputDirectory = outputDir,
        executor = executor,
        mediaActionSound = sound,
        footerContent = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier
                        .size(Dimens.Three)
                        .clickable {
                            navigateToMedia()
                        },
                    painter = painterResource(
                        id = R.drawable.arrow_down
                    ),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(
                        color = MaterialTheme.colors.onBackground
                    )
                )
                Spacer(modifier = Modifier.height(Dimens.One))
                MediaHorizontalList(
                    lazyMediaList = mediaList
                )
            }
        },
        onImageCaptured = onImageCaptured,
        onError = onError
    )
}

@Composable
internal fun MediaHorizontalList(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    lazyMediaList: LazyPagingItems<MediaData>
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        state = state,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(
            count = lazyMediaList.itemCount,
            key = {
                lazyMediaList[it]?.id ?: it
            },
            itemContent =  {
                lazyMediaList[it]?.let { media ->
                    MediaView(
                        modifier = Modifier.size(Dimens.Nine),
                        uri = media.uri ?: Uri.EMPTY,
                        isSelected = media.selected
                    )
                }
            }
        )
    }
}

@Preview
@Composable
private fun CameraScreenPreview() {
    
}