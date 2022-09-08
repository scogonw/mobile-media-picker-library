package com.scogo.mediapicker.presentation.home

import android.Manifest
import android.media.MediaActionSound
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.scogo.mediapicker.common.ui_theme.ScogoTheme
import com.scogo.mediapicker.presentation.camera.CameraDataHolder
import com.scogo.mediapicker.presentation.navigation.AppNavigationParams
import com.scogo.mediapicker.utils.getMediaDirectory
import com.scogo.mediapicker.utils.isPermissionsGranted
import java.util.concurrent.Executors

class HomeActivity: ComponentActivity() {

    private val permissions = listOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cameraHolder = CameraDataHolder(
            outputDirectory = getMediaDirectory("Scogo"),
            sound = MediaActionSound(),
        )
        val appNavParams = AppNavigationParams(
            permissions = permissions,
            permissionsGranted = isPermissionsGranted(permissions),
            cameraHolder = cameraHolder,
            executor = Executors.newSingleThreadExecutor()
        )
        setContent {
            ScogoTheme {
                HomeScreen(
                    params = appNavParams
                )
            }
        }
    }

}
