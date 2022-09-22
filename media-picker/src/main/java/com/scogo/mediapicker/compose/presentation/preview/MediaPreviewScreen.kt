package com.scogo.mediapicker.compose.presentation.preview

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Crop
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.scogo.mediapicker.compose.common.custom.AddCaption
import com.scogo.mediapicker.compose.common.media.MediaPreviewListView
import com.scogo.mediapicker.compose.R
import com.scogo.mediapicker.compose.presentation.theme.Dimens
import com.scogo.mediapicker.compose.presentation.media.MediaViewModel
import com.scogo.mediapicker.compose.presentation.activityMediaViewModel
import com.scogo.mediapicker.compose.core.media.MediaData
import com.scogo.mediapicker.compose.util.isVideo
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@Composable
internal fun MediaPreviewScreen(
    onMediaPicked: () -> Unit,
    onBack: () -> Unit,
    cropImage: (MediaData) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val mediaViewModel = activityMediaViewModel()

    MediaPreviewView(
        modifier = Modifier,
        mediaViewModel = mediaViewModel,
        onMediaPicked = onMediaPicked,
        cropImage = cropImage,
        onBack = {
            scope.launch {
                mediaViewModel.clearCapturedMedia()
                onBack()
            }
        }
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun MediaPreviewView(
    modifier: Modifier,
    mediaViewModel: MediaViewModel,
    onMediaPicked: () -> Unit,
    cropImage: (MediaData) -> Unit,
    onBack: () -> Unit,
) {
    BackHandler(onBack = onBack)

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    val pagerState = rememberPagerState()

    val uiState = mediaViewModel.uiState.collectAsState()
    val selectedMediaList = mediaViewModel.selectedMediaList.collectAsState()

    val capturedMediaList = mediaViewModel.readCapturedMedia()
    val previewForCapturedMedia = capturedMediaList.isNotEmpty()

    val currentMediaList = if(previewForCapturedMedia) capturedMediaList
    else selectedMediaList.value

    val currentMedia = remember { mutableStateOf(MediaData.EMPTY) }
    val captionFieldState = remember { mutableStateOf(TextFieldValue("")) }

    LaunchedEffect(captionFieldState.value) {
        currentMedia.value.caption = captionFieldState.value.text
    }

    LaunchedEffect(mediaViewModel.mediaIndex) {
        if(mediaViewModel.mediaIndex !in setOf(0,-1)) {
            pagerState.scrollToPage(mediaViewModel.mediaIndex)
            mediaViewModel.mediaIndex = 0
        }
    }

    LaunchedEffect(uiState.value) {
        val msg = uiState.value.message
        if(msg.isNotEmpty()) {
            scaffoldState.snackbarHostState.showSnackbar(
                message = msg,
                duration = SnackbarDuration.Short
            )
            with(mediaViewModel) {
                clearMessage()
            }
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow {
            pagerState.currentPage
        }.distinctUntilChanged().collectIndexed { _, page ->
            currentMedia.value = currentMediaList[page]
            captionFieldState.value = TextFieldValue(
                text = currentMedia.value.caption ?: ""
            )
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(Color.Black),
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {},
                backgroundColor = Color.Black,
                elevation = Dimens.Zero,
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        content = {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    )
                },
                actions = {
                    if(!currentMedia.value.mimeType.isVideo()) {
                        IconButton(
                            onClick = {
                                cropImage(currentMedia.value)
                            },
                            content = {
                                Icon(
                                    imageVector = Icons.Default.Crop,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                        )
                    }
                }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Color.Black)
                ,
                content =  {
                    BoxWithConstraints(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center,
                        content =  {
                            MediaHorizontalPager(
                                modifier = Modifier.fillMaxSize(),
                                viewModifier = Modifier
                                    .fillMaxWidth()
                                    .height(this@BoxWithConstraints.maxHeight / 2),
                                state = pagerState,
                                mediaList = currentMediaList
                            )
                            AddCaption(
                                modifier = Modifier
                                    .imePadding()
                                    .navigationBarsPadding()
                                    .systemBarsPadding()
                                    .align(Alignment.BottomCenter),
                                textFieldState = captionFieldState,
                                onActionClick = {
                                    if (mediaViewModel.captionMandatory()) {
                                        val index = mediaViewModel.isCaptionsEmpty(currentMediaList)
                                        if(index == -1) {
                                            onMediaPicked()
                                        }else {
                                            scope.launch {
                                                mediaViewModel.showMessage(
                                                    msg = context.getString(R.string.please_add_caption)
                                                )
                                                pagerState.animateScrollToPage(index)
                                            }
                                        }
                                    } else {
                                        onMediaPicked()
                                    }
                                }
                            )
                        }
                    )
                }
            )
        }
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
internal fun MediaHorizontalPager(
    modifier: Modifier,
    viewModifier: Modifier,
    state: PagerState,
    mediaList: List<MediaData>
) {
    HorizontalPager(
        state = state,
        count = mediaList.size,
        content = {
            MediaPreviewListView(
                modifier = modifier,
                viewModifier = viewModifier,
                media = mediaList[it]
            )
        }
    )
}