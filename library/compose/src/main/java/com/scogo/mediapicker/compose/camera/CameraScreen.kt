package com.scogo.mediapicker.compose.camera

import android.media.MediaActionSound
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.scogo.mediapicker.common.ui.components.MediaView
import com.scogo.mediapicker.common.ui_res.R
import com.scogo.mediapicker.common.ui_theme.Dimens
import com.scogo.mediapicker.compose.*
import com.scogo.mediapicker.compose.media.MediaScreen
import com.scogo.mediapicker.compose.media.MediaViewModel
import com.scogo.mediapicker.core.di.AppServiceLocator
import com.scogo.mediapicker.core.media.MediaData
import com.scogo.mediapicker.utils.FileUtil
import kotlinx.coroutines.Dispatchers
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
    navigateToPreview: (MediaData) -> Unit,
    onBackPress: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
        confirmStateChange = {
            it != ModalBottomSheetValue.HalfExpanded
        },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
        )
    )

    BackHandler {
        scope.launch {
            if (sheetState.isVisible) {
                sheetState.animatedHide()
            } else {
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
                mediaViewModel = mediaViewModel,
                mediaList = mediaList,
                navigateToPreview = navigateToPreview,
                onBack = {
                    scope.launch {
                        sheetState.animatedHide()
                    }
                }
            )
        },
        content = {
            CameraView(
                modifier = Modifier.onSwipe(
                    onUp = {
                        scope.launch {
                            sheetState.animatedShow()
                        }
                    }
                ),
                outputDirectory = outputDir,
                executor = executor,
                mediaActionSound = sound,
                footerContent = {
                    CameraFooter(
                        lazyMediaList = mediaList,
                        mediaViewModel = mediaViewModel,
                        onMediaListItemClick = {

                        },
                    )
                },
                bottomContent = {

                },
                onImageCaptured = {
                    scope.launch(Dispatchers.IO) {
                        FileUtil.saveImage(context, it)
                        mediaList.refresh()
                    }
                },
                onError = {

                }
            )
        }
    )
}

@Composable
internal fun CameraFooter(
    modifier: Modifier = Modifier,
    mediaViewModel: MediaViewModel,
    lazyMediaList: LazyPagingItems<MediaData>,
    onMediaListItemClick: (MediaData) -> Unit,
) {
    val listState = rememberLazyListState()

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(Dimens.Three),
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
            mediaViewModel = mediaViewModel,
            lazyMediaList = lazyMediaList,
            onItemClick = onMediaListItemClick
        )
    }
}

@Composable
internal fun MediaHorizontalList(
    modifier: Modifier = Modifier,
    state: LazyListState,
    mediaViewModel: MediaViewModel,
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
            itemContent = {
                lazyMediaList[it]?.let { media ->
                    if (media.uri != null) {
                        MediaView(
                            modifier = Modifier.size(Dimens.Nine),
                            media = media.also { mMedia ->
                                mMedia.selected = mediaViewModel.isMediaSelected(mMedia.id)
                            },
                            onSelectMedia = { selectMedia ->
                                mediaViewModel.selectMedia(selectMedia)
                            },
                            onItemClick = onItemClick
                        )
                    }
                }
            }
        )
    }
}
