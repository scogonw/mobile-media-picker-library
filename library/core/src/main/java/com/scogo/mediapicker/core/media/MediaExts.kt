package com.scogo.mediapicker.core.media

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.os.bundleOf

private const val externalUri = "external"
private val projections by lazy {
    arrayOf(
        MediaStore.Files.FileColumns._ID,
        MediaStore.Files.FileColumns.DISPLAY_NAME,
        MediaStore.Files.FileColumns.DATE_ADDED,
        MediaStore.Files.FileColumns.MEDIA_TYPE,
        MediaStore.Files.FileColumns.MIME_TYPE,
    )
}

@SuppressLint("Recycle")
fun Context.createMediaCursor(
    limit: Int,
    offset: Int,
): Cursor? {
    val whereCondition = "${MediaStore.Files.FileColumns.MEDIA_TYPE} = ? OR ${MediaStore.Files.FileColumns.MEDIA_TYPE} = ?"
    val selectionArgs = arrayOf(
        "${MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE}",
        "${MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO}"
    )

    return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val bundle = bundleOf(
            ContentResolver.QUERY_ARG_OFFSET to offset,
            ContentResolver.QUERY_ARG_LIMIT to limit,
            ContentResolver.QUERY_ARG_SORT_COLUMNS to arrayOf(MediaStore.Files.FileColumns.DATE_ADDED),
            ContentResolver.QUERY_ARG_SORT_DIRECTION to ContentResolver.QUERY_SORT_DIRECTION_DESCENDING,
            ContentResolver.QUERY_ARG_SQL_SELECTION to whereCondition,
            ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS to selectionArgs
        )
        contentResolver.query(
            MediaStore.Files.getContentUri(externalUri),
            projections,
            bundle,
            null,
        )
    }else {
        contentResolver.query(
            MediaStore.Files.getContentUri(externalUri),
            projections,
            whereCondition,
            selectionArgs,
            "${MediaStore.Files.FileColumns.DATE_ADDED} DESC LIMIT $limit OFFSET $offset",
            null
        )
    }
}

fun Context.fetchMedia(
    limit: Int,
    offset: Int,
): List<MediaData> {
    val mediaList = arrayListOf<MediaData>()
    val cursor = createMediaCursor(limit, offset)
    cursor?.let {
        val idColumn = it.getColumnIndexOrThrow(projections[0])
        val displayNameColumn = it.getColumnIndexOrThrow(projections[1])
        val dateTakenColumn = it.getColumnIndexOrThrow(projections[2])
        val mediaTypeColumn = it.getColumnIndexOrThrow(projections[3])
        val mimeTypeColumn = it.getColumnIndexOrThrow(projections[4])

        while(it.moveToNext()) {
            val id = it.getLong(idColumn)
            val displayName = it.getString(displayNameColumn)
            val dateTaken = it.getLong(dateTakenColumn)
            val mediaType = it.getInt(mediaTypeColumn)
            val mimeType = it.getString(mimeTypeColumn)
            val contentUri = ContentUris.withAppendedId(
                getImageVideoUri(),
                id
            )
            mediaList.add(
                MediaData(
                    id = id,
                    uri = contentUri,
                    mimeType = mimeType,
                    mediaType = mediaType,
                    displayName = displayName,
                    dateTaken = dateTaken,
                    caption = null,
                    bucketName = null,
                    selected = false
                )
            )
        }
    }
    cursor?.close()
    return mediaList
}

private fun getImageVideoUri(): Uri {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)
    } else {
        MediaStore.Files.getContentUri(externalUri)
    }
}
