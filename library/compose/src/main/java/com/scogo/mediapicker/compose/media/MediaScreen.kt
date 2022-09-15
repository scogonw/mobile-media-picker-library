package com.scogo.mediapicker.compose.media

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.paging.compose.LazyPagingItems
import com.scogo.mediapicker.common.ui.components.custom.BottomActionBar
import com.scogo.mediapicker.common.ui.components.media.MediaView
import com.scogo.mediapicker.common.ui_res.R
import com.scogo.mediapicker.common.ui_theme.Dimens
import com.scogo.mediapicker.core.media.MediaData

@Composable
internal fun MediaScreen(
    modifier: Modifier = Modifier,
    mediaViewModel: MediaViewModel,
    mediaList: LazyPagingItems<MediaData>,
    navigateToPreview: () -> Unit,
    onBack: () -> Unit,
) {
    val scaffoldState = rememberScaffoldState()
    val selectedMedia = mediaViewModel.selectedMediaList.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        content = {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = null
                            )
                        }
                    )
                },
                backgroundColor = MaterialTheme.colors.background
            )
        },
        bottomBar = {
            val visible = remember { mutableStateOf(false) }
            visible.value = selectedMedia.value.isNotEmpty()
            AnimatedVisibility(
                visible = visible.value,
                enter = slideInVertically() + fadeIn(),
                exit = shrinkOut(spring(Spring.DampingRatioHighBouncy)) + fadeOut(),
            ) {
                BottomActionBar(
                    modifier = Modifier.fillMaxWidth(),
                    header = stringResource(R.string.view_selected),
                    icon = Icons.Default.Image,
                    actionName = stringResource(R.string.add_with_count, selectedMedia.value.size),
                    onActionClick = navigateToPreview
                )
            }
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding)
            ) {
                val listState = rememberLazyListState()
                MediaVerticalGridList(
                    state = listState,
                    lazyMediaList = mediaList,
                    onItemClick = {
                        mediaViewModel.selectMedia(it)
                    }
                )
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun MediaVerticalGridList(
    modifier: Modifier = Modifier,
    state: LazyListState,
    lazyMediaList: LazyPagingItems<MediaData>,
    onItemClick: (MediaData) -> Unit,
) {
    val rowSize = 4
    val snapshotItems = lazyMediaList.itemSnapshotList.items
    val list = snapshotItems.groupBy { it.date }

    LazyColumn(
        modifier = modifier,
        state = state
    ) {
        list.forEach { (date, items) ->
            if (!date.isNullOrEmpty()) {
                stickyHeader {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colors.background)
                    ) {
                        Text(
                            modifier = Modifier.padding(Dimens.One),
                            color = Color.DarkGray,
                            style = MaterialTheme.typography.subtitle2,
                            text = date.uppercase(),
                        )
                    }
                }
            }

            items(
                items.chunked(rowSize),
            ) { row ->
                Row(Modifier.fillParentMaxWidth()) {
                    for ((index, media) in row.withIndex()) {
                        Box(Modifier.fillMaxWidth(1f / (rowSize - index))) {
                            if (media.uri != null) {
                                MediaView(
                                    modifier = Modifier
                                        .height(Dimens.Twelve)
                                        .padding(Dimens.OneHalf),
                                    media = media,
                                    onItemClick = onItemClick
                                )
                            }
                        }
                    }
                }
            }
            lazyMediaList[snapshotItems.size - 1]
        }
    }
}


