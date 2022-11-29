package com.scogo.mediapicker.compose.util

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

internal fun Activity.isPermissionsGranted(
    permissions: List<String>
): Boolean {
    var granted = true
    for(i in permissions) {
        if(ContextCompat.checkSelfPermission(this,i) != PackageManager.PERMISSION_GRANTED) {
            granted = false
            break
        }
    }
    return granted
}