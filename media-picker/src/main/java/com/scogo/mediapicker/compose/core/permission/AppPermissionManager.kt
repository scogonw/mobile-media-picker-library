package com.scogo.mediapicker.compose.core.permission

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

interface AppPermissionManagerCallbacks {
    fun checkAndRequestAppPermissions()
    fun dispose()
}

class AppPermissionManager private constructor(
    private var context: Activity?,
    private var owner: LifecycleOwner?,
) : AppPermissionManagerCallbacks, LifecycleObserver {

    companion object {
        const val REQUEST_APP_PERMISSIONS_CODE = 99

        fun register(context: Activity?, owner: LifecycleOwner?): AppPermissionManager {
            val manager = AppPermissionManager(
                context = context, owner = owner
            )
            owner?.lifecycle?.addObserver(manager)
            return manager
        }
    }

    private val commonAppPermissions = listOf(
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO
    )

    /**
     * READ_MEDIA_IMAGES and READ_MEDIA_VIDEO
     * required only for android 13 or TIRAMISU (api level 33)
     * if app want to access other apps media file
     */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val readMediaPermissions = listOf(
        Manifest.permission.READ_MEDIA_IMAGES,
        Manifest.permission.READ_MEDIA_VIDEO,
    )

    /**
     * READ_EXTERNAL_STORAGE
     * required only below android 13 or TIRAMISU (max api level 32)
     * if app want to access other apps media files
     */
    private val readExternalStoragePermission: String
        get() {
            return Manifest.permission.READ_EXTERNAL_STORAGE
        }

    /**
     * WRITE_EXTERNAL_STORAGE
     * required only below android 10 OR Q (max api level 29)
     */
    private val writeExternalStoragePermission: String
        get() {
            return Manifest.permission.WRITE_EXTERNAL_STORAGE
        }

    private val appPermissions: List<String>
        get() {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                commonAppPermissions + readMediaPermissions
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                commonAppPermissions + readExternalStoragePermission
            } else {
                commonAppPermissions + readExternalStoragePermission + writeExternalStoragePermission
            }
        }

    fun requiredAppPermissions() = appPermissions

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    override fun dispose() {
        context = null
        owner?.lifecycle?.removeObserver(this)
        owner = null
    }

    override fun checkAndRequestAppPermissions() {
        if (!isPermissionsGranted(appPermissions)) {
            requestPermissions(appPermissions)
        }
    }

    private fun requestPermissions(permissions: List<String>): Boolean {
        if (context == null) return false
        ActivityCompat.requestPermissions(
            context!!,
            permissions.toTypedArray(),
            REQUEST_APP_PERMISSIONS_CODE
        )
        return true
    }

    fun isAppPermissionsGranted(): Boolean {
        if (context == null) return false
        val allGranted = appPermissions.map { isPermissionGranted(it) }
        return allGranted.all { it }
    }

    private fun isPermissionGranted(permission: String): Boolean {
        if (context == null) return false
        val granted = ContextCompat.checkSelfPermission(context!!, permission)
        return granted == PackageManager.PERMISSION_GRANTED
    }

    private fun isPermissionsGranted(permissions: List<String>): Boolean {
        if (context == null) return false
        val allGranted = permissions.map { isPermissionGranted(it) }
        return allGranted.all { it }
    }
}
