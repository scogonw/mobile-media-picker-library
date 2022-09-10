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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.scogo.mediapicker.common.ui.components.MediaView
import com.scogo.mediapicker.common.ui_res.R
import com.scogo.mediapicker.common.ui_theme.Dimens
import com.scogo.mediapicker.compose.activityMediaViewModel
import com.scogo.mediapicker.compose.composeActivity
import com.scogo.mediapicker.compose.media.MediaScreen
import com.scogo.mediapicker.compose.media.MediaViewModel
import com.scogo.mediapicker.core.di.AppServiceLocator
import com.scogo.mediapicker.core.media.MediaData
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.Executor

@Composable
internal fun CameraScreen(
    outputDir: File,
    navigateToPreview: (MediaData) -> Unit,
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
        navigateToPreview = navigateToPreview,
        onBackPress = {
            activity.finish()
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun CameraScreen(
    mediaViewModel: MediaViewModel,
    sound: MediaActionSound,
    outputDir: File,
    executor: Executor,
    onImageCaptured: (Uri) -> Unit,
    onError: (Exception) -> Unit,
    navigateToPreview: (MediaData) -> Unit,
    onBackPress: () -> Unit,
) {
    val scope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange =  {
            it != ModalBottomSheetValue.HalfExpanded
        }
    )

    BackHandler {
        scope.launch {
            if(sheetState.isVisible) {
                sheetState.hide()
            }else {
                onBackPress()
            }
        }
    }

    val mediaList = mediaViewModel.getMediaList().collectAsLazyPagingItems()

    ModalBottomSheetLayout(
        modifier = Modifier.fillMaxSize(),
        sheetState = sheetState,
        sheetContent = {
            MediaScreen(
                navigateToPreview = navigateToPreview
            )
        },
        content =  {
            CameraView(
                outputDirectory = outputDir,
                executor = executor,
                mediaActionSound = sound,
                footerContent = {
                    CameraFooter(
                        lazyMediaList = mediaList,
                        onMediaListItemClick = navigateToPreview,
                        navigateToMedia = {
                            scope.launch {
                                sheetState.animateTo(ModalBottomSheetValue.Expanded)
                            }
                        },
                    )
                },
                onImageCaptured = onImageCaptured,
                onError = onError
            )
        }
    )
}

@Composable
internal fun CameraFooter(
    modifier: Modifier = Modifier,
    lazyMediaList: LazyPagingItems<MediaData>,
    navigateToMedia: () -> Unit,
    onMediaListItemClick: (MediaData) -> Unit,
) {
    val listState = rememberLazyListState()

    Column(
        modifier = modifier.fillMaxWidth(),
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
                color = Color.White
            )
        )

        Spacer(modifier = Modifier.height(Dimens.One))

        MediaHorizontalList(
            state = listState,
            lazyMediaList = lazyMediaList,
            onItemClick = onMediaListItemClick
        )
    }
}

@Composable
internal fun MediaHorizontalList(
    modifier: Modifier = Modifier,
    state: LazyListState,
    lazyMediaList: LazyPagingItems<MediaData>,
    onItemClick: (MediaData) -> Unit,
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
                    media.uri?.let { uri ->
                        MediaView(
                            modifier = Modifier
                                .size(Dimens.Nine)
                                .clickable {
                                    onItemClick(media)
                                }
                            ,
                            uri = uri,
                            isSelected = media.selected
                        )
                    }
                }
            }
        )
    }
}
