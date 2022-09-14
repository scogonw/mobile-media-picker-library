package com.scogo.mediapicker.compose.home

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.scogo.mediapicker.common.ui_theme.ScogoTheme
import com.scogo.mediapicker.compose.media.MediaViewModel
import com.scogo.mediapicker.compose.media.MediaViewModelFactory
import com.scogo.mediapicker.compose.navigation.AppNavigationParams
import com.scogo.mediapicker.core.data.impl.MediaRepositoryImpl
import com.scogo.mediapicker.utils.Consts.WORK_ID
import com.scogo.mediapicker.utils.getScogoMediaDirectory
import com.scogo.mediapicker.utils.isPermissionsGranted


internal class HomeActivity : ComponentActivity() {

    val mediaViewModel: MediaViewModel by viewModels {
        MediaViewModelFactory(MediaRepositoryImpl(this))
    }

    private val permissions by lazy {
        listOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val workId = intent.extras?.getString(WORK_ID)
        if(!mediaViewModel.initRequestData(workId)) {
            finish()
        }

        val appNavParams = AppNavigationParams(
            permissions = permissions,
            permissionsGranted = isPermissionsGranted(permissions),
            scogoMediaDir = getScogoMediaDirectory()
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
