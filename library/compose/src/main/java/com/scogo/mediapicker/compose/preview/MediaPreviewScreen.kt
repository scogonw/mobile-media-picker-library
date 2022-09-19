package com.scogo.mediapicker.compose.preview

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.scogo.mediapicker.common.ui.components.custom.IconRoundedButton
import com.scogo.mediapicker.common.ui.components.media.MediaPreviewListView
import com.scogo.mediapicker.common.ui_res.R.string
import com.scogo.mediapicker.common.ui_theme.ButtonDimes
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
                                    .height(this@BoxWithConstraints.maxHeight / 2),
                                state = pagerState,
                                mediaList = capturedImages.ifEmpty {
                                    selectedImages.value
                                }
                            )
                        }
                    )
                    AddCaption(modifier = Modifier.fillMaxWidth())
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

@Composable
fun AddCaption(
    modifier: Modifier,
    onActionClick: () -> Unit = {}
) {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.One, vertical = Dimens.Two),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            modifier = Modifier
                .height(ButtonDimes.Six)
                .weight(1f)
                .padding(Dimens.Zero),
            value = text,
            onValueChange = {
                text = it
            },
            placeholder = {
                Text(
                    text = stringResource(id = string.enter_your_caption),
                    style = MaterialTheme.typography.subtitle2,
                )
            },
            shape = RoundedCornerShape(Dimens.Four),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = Color.White,
                focusedBorderColor = Color.Transparent
            ),
        )
        IconRoundedButton(
            modifier = Modifier.padding(start = Dimens.One),
            icon = Icons.Filled.Send,
            onActionClick
        )
    }
}

@Preview
@Composable
fun AddCaptionPreview() {
    AddCaption(
        modifier = Modifier,
        onActionClick = {}
    )
}
