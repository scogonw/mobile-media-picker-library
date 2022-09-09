package com.scogo.mediapicker.utils

import android.app.Activity
import java.io.File

fun Activity.getScogoMediaDirectory(): File {
    return getMediaDirectory("Scogo")
}
fun Activity.getMediaDirectory(
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