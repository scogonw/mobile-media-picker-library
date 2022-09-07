package com.scogo.mediapicker.utils

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

fun Activity.isPermissionsGranted(
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