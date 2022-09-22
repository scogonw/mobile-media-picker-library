package com.scogo.mediapicker.compose.presentation.camera

import android.app.Activity
import android.media.MediaActionSound
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.scogo.mediapicker.compose.common.custom.BottomActionBar
import com.scogo.mediapicker.compose.common.media.MediaView
import com.scogo.mediapicker.compose.R
import com.scogo.mediapicker.compose.presentation.theme.Dimens
import com.scogo.mediapicker.compose.presentation.media.MediaScreen
import com.scogo.mediapicker.compose.presentation.media.MediaViewModel
import com.scogo.mediapicker.compose.util.*
import com.scogo.mediapicker.compose.core.di.AppServiceLocator
import com.scogo.mediapicker.compose.core.media.MediaData
import com.scogo.mediapicker.compose.presentation.activityMediaViewModel
import com.scogo.mediapicker.compose.presentation.composeActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.Executor

@Composable
internal fun CameraScreen(
    outputDir: File,
    navigateToPreview: (workId: String, activity: Activity) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val activity = composeActivity()
    val mediaViewModel = activityMediaViewModel()

    CameraScreen(
        mediaViewModel = mediaViewModel,
        sound = AppServiceLocator.mediaSound,
        outputDir = outputDir,
        executor = AppServiceLocator.executor,
        navigateToPreview = {
            navigateToPreview(
                mediaViewModel.readRequestData().readId(),
                activity
            )
        },
        clearMediaSelection = {
            scope.launch {
                mediaViewModel.clearMediaSelection()
            }
        },
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
    navigateToPreview: () -> Unit,
    clearMediaSelection: () -> Unit,
    onBackPress: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val mediaList = mediaViewModel.getMediaList().collectAsLazyPagingItems()
    val selectedMedia = mediaViewModel.selectedMediaList.collectAsState()

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

    ModalBottomSheetLayout(
        modifier = Modifier.fillMaxSize(),
        sheetState = sheetState,
        sheetContent = {
            MediaScreen(
                mediaViewModel = mediaViewModel,
                mediaList = mediaList,
                navigateToPreview = navigateToPreview,
                clearMediaSelection = clearMediaSelection,
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
                        onMediaListItemClick = {
                            scope.launch {
                                mediaViewModel.selectMedia(it)
                            }
                        },
                    )
                },
                bottomContent = {
                    AnimatedVisibility(
                        visible = selectedMedia.value.isNotEmpty(),
                        enter = slideInVertically() + fadeIn(),
                        exit = shrinkOut(spring(Spring.DampingRatioHighBouncy)) + fadeOut(),
                    ) {
                        BottomActionBar(
                            modifier = Modifier.fillMaxWidth(),
                            header = stringResource(R.string.view_selected),
                            icon = Icons.Default.Image,
                            actionName = stringResource(R.string.add_with_count, selectedMedia.value.size),
                            onActionClick = navigateToPreview,
                            onDismiss = clearMediaSelection
                        )
                    }
                },
                onMediaCaptured = { uri ->
                    scope.launch(Dispatchers.IO) {
                        FileUtil.saveImageIfNotVideo(context,uri)?.let {
                            val media = MediaData.create(
                                uri = it,
                                mime = FileUtil.getMimeType(it,context)
                            )
                            mediaViewModel.writeCapturedMedia(listOf(media))
                            mediaList.refresh()
                            navigateToPreview()
                        }
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
                id = R.drawable.arrow_up
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
            itemContent = {
                lazyMediaList[it]?.let { media ->
                    if (media.uri != null) {
                        MediaView(
                            modifier = Modifier.size(Dimens.Nine),
                            media = media,
                            onItemClick = onItemClick
                        )
                    }
                }
            }
        )
    }
}
