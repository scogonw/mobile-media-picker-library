package com.scogo.mediapicker.presentation.home

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.scogo.mediapicker.common.ui_theme.ScogoTheme
import com.scogo.mediapicker.presentation.navigation.AppNavigationParams
import com.scogo.mediapicker.utils.isPermissionsGranted

class HomeActivity: ComponentActivity() {

    private val permissions = listOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appNavParams = AppNavigationParams(
            permissions = permissions,
            permissionsGranted = isPermissionsGranted(permissions)
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
