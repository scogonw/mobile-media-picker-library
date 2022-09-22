package com.scogo.mediapicker.compose.presentation.home

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.scogo.mediapicker.compose.presentation.theme.ScogoTheme
import com.scogo.mediapicker.compose.presentation.media.MediaViewModel
import com.scogo.mediapicker.compose.presentation.media.MediaViewModelFactory
import com.scogo.mediapicker.compose.presentation.navigation.AppNavigationParams
import com.scogo.mediapicker.compose.core.data.impl.MediaRepositoryImpl
import com.scogo.mediapicker.compose.core.event.PushEvent
import com.scogo.mediapicker.compose.util.Consts.WORK_ID
import com.scogo.mediapicker.compose.util.getScogoMediaDirectory
import com.scogo.mediapicker.compose.util.isPermissionsGranted
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

internal class HomeActivity : ComponentActivity() {

    val mediaViewModel: MediaViewModel by viewModels {
        MediaViewModelFactory(MediaRepositoryImpl(this))
    }

    private val permissions by lazy {
        listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val workId = intent.extras?.getString(WORK_ID)
        if(!mediaViewModel.initRequestData(workId)) finish()

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

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onEvent(event: PushEvent) {
        if(event.workId == mediaViewModel.readRequestData().readId()) {
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        if(!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onStop() {
        super.onStop()
        if(EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }
}