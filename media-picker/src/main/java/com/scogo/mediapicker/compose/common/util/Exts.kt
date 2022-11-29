package com.scogo.mediapicker.compose.common.util

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.scogo.mediapicker.compose.R
import kotlinx.coroutines.delay

@SuppressLint("CheckResult")
internal fun ImageView.load(
    source: Any?,
    centerCrop: Boolean = true,
    @DrawableRes placeholder: Int = R.drawable.image_placeholder,
): ImageView {
    val options = RequestOptions().apply {
        if(centerCrop) centerCrop()
        placeholder(placeholder)
        diskCacheStrategy(DiskCacheStrategy.ALL)
    }
    Glide.with(context)
        .load(source)
        .apply(options)
        .into(this)
    return this
}

internal fun View.maxSize(): View {
    val params = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.MATCH_PARENT
    )
    layoutParams = params
    return this
}

@SuppressLint("CheckResult")
@Composable
internal fun loadThumbnail(
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
internal fun FastAsyncImage(
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

internal fun LazyListState.isScrolledToTheEnd(): Boolean {
    return layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1
}
internal fun LazyListState.isScrolledToTheNearEnd(
    size: Int = 10
): Boolean {
    var atNearBottom = false
    val lastIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index
    val count = layoutInfo.totalItemsCount
    for(i in 0..size) {
        atNearBottom = (lastIndex == count - i)
    }
    return atNearBottom
}

/**
 * Requires State<Boolean>
 * val interactionSource = remember { MutableInteractionSource() }
 * val isPressed by interactionSource.collectIsPressedAsState()
 *
 * isPressed.detectGestures(...)
 */
@Composable
internal fun DetectGestures(
    pressedState: State<Boolean>,
    onClick: () -> Unit,
    onHold: (released: Boolean) -> Unit,
) {
    val wasClickEvent = remember { mutableStateOf(false) }
    val isHoldActive = remember { mutableStateOf(false) }

    LaunchedEffect(pressedState.value) {
        if(pressedState.value) {
            wasClickEvent.value = true
            delay(300)
            isHoldActive.value = true
            onHold(false)
        } else {
            if(isHoldActive.value) {
                onHold(true)
            }else if(wasClickEvent.value){
                onClick()
            }
            isHoldActive.value = false
            wasClickEvent.value = false
        }
    }
}