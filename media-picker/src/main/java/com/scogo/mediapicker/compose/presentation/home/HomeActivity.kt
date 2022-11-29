package com.scogo.mediapicker.compose.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.scogo.mediapicker.common.ui_theme.ScogoTheme
import com.scogo.mediapicker.compose.media.MediaViewModel
import com.scogo.mediapicker.compose.media.MediaViewModelFactory
import com.scogo.mediapicker.compose.navigation.AppNavigationParams
import com.scogo.mediapicker.core.data.impl.MediaRepositoryImpl
import com.scogo.mediapicker.core.event.PushEvent
import com.scogo.mediapicker.core.permission.AppPermissionManager
import com.scogo.mediapicker.utils.Consts.WORK_ID
import com.scogo.mediapicker.utils.getScogoMediaDirectory
import com.scogo.mediapicker.utils.isPermissionsGranted
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

internal class HomeActivity : ComponentActivity() {

    private var manager: AppPermissionManager? = null

    val mediaViewModel: MediaViewModel by viewModels {
        MediaViewModelFactory(MediaRepositoryImpl(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        manager = AppPermissionManager.register(this, this)
        val permissions = manager?.requiredAppPermissions() ?: emptyList()

        val workId = intent.extras?.getString(WORK_ID)
        if (!mediaViewModel.initRequestData(workId)) finish()

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
        if (event.workId == mediaViewModel.readRequestData().readId()) {
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onStop() {
        super.onStop()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }
}
