package com.scogo.mediapicker.compose.media

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.compose.collectAsLazyPagingItems
import com.scogo.mediapicker.common.ui.components.MediaView
import com.scogo.mediapicker.common.ui_theme.Dimens
import com.scogo.mediapicker.compose.activityMediaViewModel

@Composable
internal fun MediaScreen(
    navigateToPreview: () -> Unit
) {
    MediaScreenView(
        modifier = Modifier,
        navigateToPreview = navigateToPreview,
        mediaViewModel = activityMediaViewModel()
    )
}

@Composable
private fun MediaScreenView(
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    mediaViewModel: MediaViewModel,
    navigateToPreview: () -> Unit,
) {
    val mediaList = mediaViewModel.getMediaList().collectAsLazyPagingItems()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        content = { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding)
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(
                        minSize = Dimens.Sixteen
                    ),
                    contentPadding = PaddingValues(
                        horizontal = Dimens.One,
                        vertical = Dimens.One
                    ),
                    content = {
                        items(
                            count = mediaList.itemCount,
                            key = {
                                mediaList[it]?.id ?: it
                            }
                        ) {
                            mediaList[it]?.let { media ->
                                MediaView(
                                    modifier = Modifier.size(Dimens.Sixteen),
                                    uri = media.uri ?: Uri.EMPTY,
                                    isSelected = media.selected
                                )
                            }
                        }
                    }
                )
            }
        }
    )
}

@Preview
@Composable
private fun Preview() {

}