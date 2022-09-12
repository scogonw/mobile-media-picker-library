package com.scogo.mediapicker.common.ui.components

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.scogo.mediapicker.common.ui_res.R

@SuppressLint("CheckResult")
@Composable
fun loadThumbnail(
    uri: Uri?
): MutableState<Bitmap?> {
    val state = remember { mutableStateOf<Bitmap?>(null)}
    val options = RequestOptions().apply {
        diskCacheStrategy(DiskCacheStrategy.ALL)
        override(200)
    }
    Glide.with(LocalContext.current)
        .asBitmap()
        .load(uri)
        .apply(options)
        .into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                state.value = resource
            }
            override fun onLoadCleared(placeholder: Drawable?) {

            }
            override fun onLoadFailed(errorDrawable: Drawable?) {
                state.value = null
            }
        })
    return state
}

@Composable
fun FastAsyncImage(
    modifier: Modifier,
    model: Any?,
    @DrawableRes placeholder: Int = R.drawable.image_placeholder,
){
    val request = ImageRequest
        .Builder(LocalContext.current)
        .data(model)
        .placeholder(placeholder)
        .allowHardware(false)
        .diskCachePolicy(CachePolicy.ENABLED)
        .size(200)
        .build()

    AsyncImage(
        modifier = modifier,
        model = request,
        contentScale = ContentScale.Crop,
        contentDescription = null
    )
}

fun LazyListState.isScrolledToTheEnd(): Boolean {
    return layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1
}
fun LazyListState.isScrolledToTheNearEnd(): Boolean {
    var atNearBottom = false
    val lastIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index
    val count = layoutInfo.totalItemsCount
    for(i in 0..8) {
        atNearBottom = (lastIndex == count - i)
    }
    return atNearBottom
}
@Composable
fun <T: Any> LazyGridFor(
    modifier: Modifier,
    state: LazyListState,
    lazyList: LazyPagingItems<T>,
    items: List<T>,
    rowSize: Int,
    itemContent: @Composable BoxScope.(T) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        state = state
    ) {
        items(items = items.chunked(rowSize)) { row ->
            Row(Modifier.fillParentMaxWidth()) {
                for ((index, item) in row.withIndex()) {
                    Box(Modifier.fillMaxWidth(1f / (rowSize - index))) {
                        itemContent(item)
                    }
                }
            }
        }
    }

    val atBottom by remember { derivedStateOf { state.isScrolledToTheNearEnd() }}
    LaunchedEffect(atBottom) {
        if(items.isNotEmpty() && lazyList.itemCount > 0) {
            lazyList[items.size - 1]
        }
    }
}