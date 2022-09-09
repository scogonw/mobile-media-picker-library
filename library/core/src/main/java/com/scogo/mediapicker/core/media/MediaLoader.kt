package com.scogo.mediapicker.core.media

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import com.scogo.mediapicker.utils.createCursor
import com.scogo.mediapicker.utils.projections

fun Context.fetchPictures(
    limit: Int,
    offset: Int,
): List<MediaData> {
    val mediaList = arrayListOf<MediaData>()
    val cursor = createCursor(limit, offset)
    cursor?.let {
        val idColumn = it.getColumnIndexOrThrow(projections[0])
        val displayNameColumn = it.getColumnIndexOrThrow(projections[1])
        val dateTakenColumn = it.getColumnIndexOrThrow(projections[2])
        val mimeTypeColumn = it.getColumnIndexOrThrow(projections[3])
        val bucketDisplayName = it.getColumnIndexOrThrow(projections[4])

        while(it.moveToNext()) {
            val id = it.getLong(idColumn)
            val displayName = it.getString(displayNameColumn)
            val dateTaken = it.getLong(dateTakenColumn)
            val mimeType = it.getString(mimeTypeColumn)
            val bucketName = it.getString(bucketDisplayName)
            val contentUri = ContentUris.withAppendedId(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                id
            )
            mediaList.add(
                MediaData(
                    id = id,
                    uri = contentUri,
                    mimeType = mimeType,
                    displayName = displayName,
                    dateTaken = dateTaken,
                    caption = null,
                    bucketName = bucketName,
                    selected = false
                )
            )
        }
    }
    cursor?.close()
    return mediaList
}
