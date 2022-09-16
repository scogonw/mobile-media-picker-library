package com.scogo.mediapicker.compose.preview

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.scogo.mediapicker.common.ui.components.media.MediaPreviewListView
import com.scogo.mediapicker.common.ui_theme.Dimens
import com.scogo.mediapicker.compose.media.MediaViewModel
import com.scogo.mediapicker.compose.util.activityMediaViewModel
import com.scogo.mediapicker.core.media.MediaData
import kotlinx.coroutines.launch

@Composable
internal fun MediaPreviewScreen(
    onBack: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val mediaViewModel = activityMediaViewModel()

    MediaPreviewView(
        modifier = Modifier,
        mediaViewModel = mediaViewModel,
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
    onBack: () -> Unit,
) {
    val scaffoldState = rememberScaffoldState()
    val pagerState = rememberPagerState()

    val selectedImages = mediaViewModel.selectedMediaList.collectAsState()
    val capturedImages = mediaViewModel.readCapturedMedia()

    BackHandler(onBack = onBack)

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
        ,
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {},
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
                backgroundColor = Color.Black,
                elevation = Dimens.Zero
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
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(this@BoxWithConstraints.maxHeight / 2)
                                ,
                                state = pagerState,
                                mediaList = capturedImages.ifEmpty {
                                    selectedImages.value
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
    state: PagerState,
    mediaList: List<MediaData>
) {
    HorizontalPager(
        state = state,
        count = mediaList.size,
        content = {
            MediaPreviewListView(
                modifier = modifier,
                media = mediaList[it]
            )
        }
    )
}

@Preview
@Composable
private fun Preview() {
    MediaPreviewView(
        modifier = Modifier,
        mediaViewModel = activityMediaViewModel(),
        onBack = {

        }
    )
}