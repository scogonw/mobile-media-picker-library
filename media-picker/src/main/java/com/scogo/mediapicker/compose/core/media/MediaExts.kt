package com.scogo.mediapicker.compose.core.media

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.os.bundleOf
import com.scogo.mediapicker.compose.util.convertTimeToStringDate
import kotlinx.coroutines.flow.MutableStateFlow

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
internal fun Context.createMediaCursor(
    limit: Int,
    offset: Int,
    mime: MimeTypes
): Cursor? {
    val whereCondition = "${MediaStore.Files.FileColumns.MEDIA_TYPE} = ? OR ${MediaStore.Files.FileColumns.MEDIA_TYPE} = ?"
    val imageColumn = "${MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE}"
    val videoColumn = "${MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO}"
    val selectionArgs = mutableListOf<String>().also {
        when (mime) {
            MimeTypes.IMAGE -> it.add(imageColumn)
            MimeTypes.VIDEO -> it.add(videoColumn)
            else -> {
                it.add(imageColumn)
                it.add(videoColumn)
            }
        }
    }.toTypedArray()

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

internal fun Context.fetchMedia(
    limit: Int,
    offset: Int,
    mime : MimeTypes,
): List<MediaData> {
    val mediaList = arrayListOf<MediaData>()
    val cursor = createMediaCursor(
        limit = limit,
        offset = offset,
        mime = mime
    )
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
                    date = convertTimeToStringDate(this,dateTaken),
                    caption = null,
                    bucketName = null,
                    selected = MutableStateFlow(false),
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
