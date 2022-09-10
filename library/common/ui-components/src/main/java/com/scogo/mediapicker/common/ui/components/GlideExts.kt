package com.scogo.mediapicker.common.ui.components

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

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