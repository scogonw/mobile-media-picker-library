package com.scogo.mediapicker.compose.media

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.paging.compose.LazyPagingItems
import com.scogo.mediapicker.common.ui.components.MediaView
import com.scogo.mediapicker.common.ui_theme.Dimens
import com.scogo.mediapicker.core.media.MediaData

@Composable
internal fun MediaScreen(
    modifier: Modifier = Modifier,
    mediaViewModel: MediaViewModel,
    mediaList: LazyPagingItems<MediaData>,
    navigateToPreview: (MediaData) -> Unit,
    onBack: () -> Unit,
) {
    val scaffoldState = rememberScaffoldState()

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
        content = { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding)
            ) {
                val listState = rememberLazyListState()
                MediaVerticalGridList(
                    state = listState,
                    mediaViewModel = mediaViewModel,
                    lazyMediaList = mediaList,
                    onItemClick = {

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
    mediaViewModel: MediaViewModel,
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
            if(!date.isNullOrEmpty()) {
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
                            if(media.uri != null) {
                                MediaView(
                                    modifier = Modifier
                                        .height(Dimens.Twelve)
                                        .padding(Dimens.OneHalf),
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
                }
            }
            lazyMediaList[snapshotItems.size - 1]
        }
    }
}


