package com.scogo.mediapicker.compose.media

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.scogo.mediapicker.common.ui.components.LazyGridFor
import com.scogo.mediapicker.common.ui.components.MediaView
import com.scogo.mediapicker.common.ui_theme.Dimens
import com.scogo.mediapicker.compose.activityMediaViewModel
import com.scogo.mediapicker.core.media.MediaData

@Composable
fun MediaScreen(
    modifier: Modifier = Modifier,
    navigateToPreview: (MediaData) -> Unit,
    onBack: () -> Unit,
) {
    val viewModel = activityMediaViewModel()
    val scaffoldState = rememberScaffoldState()

    val mediaList = viewModel.getMediaList().collectAsLazyPagingItems()

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
                    lazyMediaList = mediaList,
                    onItemClick = navigateToPreview
                )
            }
        }
    )
}

@Composable
internal fun MediaVerticalGridList(
    modifier: Modifier = Modifier,
    state: LazyListState,
    lazyMediaList: LazyPagingItems<MediaData>,
    onItemClick: (MediaData) -> Unit,
) {
    LazyGridFor(
        modifier = modifier,
        state = state,
        lazyList = lazyMediaList,
        items = lazyMediaList.itemSnapshotList.items,
        rowSize = 4
    ) { media ->
        if(media.uri != null) {
            MediaView(
                modifier = Modifier
                    .height(Dimens.Twelve)
                    .padding(Dimens.OneHalf)
                    .clickable {
                        onItemClick(media)
                    }
                ,
                media = media
            )
        }
    }
}


