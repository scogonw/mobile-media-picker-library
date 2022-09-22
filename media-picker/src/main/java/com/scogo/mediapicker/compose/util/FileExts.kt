package com.scogo.mediapicker.compose.util

import android.app.Activity
import java.io.File

internal fun Activity.getScogoMediaDirectory(): File {
    return getMediaDirectory("Scogo")
}
internal fun Activity.getMediaDirectory(
    name: String,
): File {
    val mediaDir = externalMediaDirs.firstOrNull()?.let {
        File(it,name).apply {
            if(!it.exists()) {
                mkdirs()
            }
        }
    }
    return if(mediaDir != null && mediaDir.exists()) {
        mediaDir
    }else {
        filesDir
    }
}
internal fun String?.isVideo(): Boolean {
    return (this ?: "").contains("video")
}