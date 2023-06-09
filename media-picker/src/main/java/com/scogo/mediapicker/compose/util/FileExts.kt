package com.scogo.mediapicker.compose.util

import android.app.Activity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

internal suspend fun Activity.getScogoMediaDirectory(): File {
    return getMediaDirectory("Scogo")
}

internal suspend fun Activity.getMediaDirectory(name: String): File = withContext(Dispatchers.IO) {
    val mediaDir = externalMediaDirs.firstOrNull()?.let {
        File(it, name).apply {
            if (!it.exists()) {
                mkdirs()
            }
        }
    }
    if (mediaDir != null && mediaDir.exists()) mediaDir
    else filesDir
}

internal fun String?.isVideo(): Boolean {
    return (this ?: "").contains("video")
}