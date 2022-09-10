package com.scogo.mediapicker.compose.media

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.scogo.mediapicker.common.ui.components.MediaView
import com.scogo.mediapicker.common.ui_theme.Dimens
import com.scogo.mediapicker.compose.activityMediaViewModel
import com.scogo.mediapicker.core.media.MediaData

@Composable
fun MediaScreen(
    modifier: Modifier = Modifier,
    navigateToPreview: (MediaData) -> Unit,
) {
    val viewModel = activityMediaViewModel()
    val scaffoldState = rememberScaffoldState()

    val mediaList = viewModel.getMediaList().collectAsLazyPagingItems()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        content = { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding)
            ) {
                val gridState = rememberLazyGridState()
                MediaVerticalGridList(
                    state = gridState,
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
    state: LazyGridState,
    lazyMediaList: LazyPagingItems<MediaData>,
    onItemClick: (MediaData) -> Unit,
) {
    LazyVerticalGrid(
        modifier = modifier,
        state = state,
        columns = GridCells.Adaptive(
            minSize = Dimens.Sixteen
        ),
        content = {
            items(
                count = lazyMediaList.itemCount,
                key = {
                    lazyMediaList[it]?.id ?: it
                }
            ) {
                lazyMediaList[it]?.let { media ->
                    media.uri?.let { uri ->
                        MediaView(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(Dimens.Sixteen)
                                .padding(Dimens.HalfQuarter)
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
        }
    )
}