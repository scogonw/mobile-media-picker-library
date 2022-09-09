package com.scogo.mediapicker.utils

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.os.Build
import android.provider.MediaStore
import androidx.core.os.bundleOf

val projections by lazy {
    arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DISPLAY_NAME,
        MediaStore.Images.Media.DATE_TAKEN,
        MediaStore.Images.Media.MIME_TYPE,
        MediaStore.Images.Media.BUCKET_DISPLAY_NAME
    )
}

@SuppressLint("Recycle")
fun Context.createCursor(
    limit: Int,
    offset: Int,
): Cursor? {
    return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val bundle = bundleOf(
            ContentResolver.QUERY_ARG_OFFSET to offset,
            ContentResolver.QUERY_ARG_LIMIT to limit,
            ContentResolver.QUERY_ARG_SORT_COLUMNS to arrayOf(MediaStore.Images.Media.DATE_ADDED),
            ContentResolver.QUERY_ARG_SORT_DIRECTION to ContentResolver.QUERY_SORT_DIRECTION_DESCENDING
        )
        contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projections,
            bundle,
            null
        )
    }else {
        contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projections,
            null,
            null,
            "${MediaStore.Images.Media.DATE_ADDED} DESC LIMIT $limit OFFSET $offset",
            null
        )
    }
}
