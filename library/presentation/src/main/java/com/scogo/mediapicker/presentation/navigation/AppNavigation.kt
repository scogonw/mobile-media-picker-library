package com.scogo.mediapicker.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.scogo.mediapicker.compose.permission.PermissionsScreen
import com.scogo.mediapicker.presentation.camera.CameraDataHolder
import com.scogo.mediapicker.presentation.camera.CameraScreen
import com.scogo.mediapicker.presentation.media.MediaScreen
import java.util.concurrent.Executor

internal sealed class Screen(val route: String) {
    object Permissions : Screen("Permissions")
    object Camera: Screen("Camera")
    object Media: Screen("Media")
}

internal sealed class NavScreen(
    private val route: String
) {
    fun createRoute(root: Screen) = "${root.route}/$route"
    fun createRoute() = "$route/$route"

    object Permissions: NavScreen("Permissions")
    object Camera: NavScreen("Camera")
    object Media: NavScreen("Media")
}

internal data class AppNavigationParams(
    val permissions: List<String>,
    val permissionsGranted: Boolean,
    val cameraHolder: CameraDataHolder,
    val executor: Executor,
)

@Composable
internal fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier,
    params: AppNavigationParams
){
    val startDestination = if(params.permissionsGranted) {
        NavScreen.Camera.createRoute()
    }else {
        NavScreen.Permissions.createRoute()
    }
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
        builder = {
            addPermissionScreen(
                navController = navController,
                root = Screen.Permissions,
                permissions = params.permissions
            )
            addCameraScreen(
                navController = navController,
                root = Screen.Camera,
                holder = params.cameraHolder,
                executor = params.executor
            )
            addMediaScreen(
                navController = navController,
                root = Screen.Media
            )
        }
    )
}

private fun NavGraphBuilder.addPermissionScreen(
    navController: NavController,
    root: Screen,
    permissions: List<String>,
) {
    composable(
        route = NavScreen.Permissions.createRoute(root),
        content = {
            PermissionsScreen(
                permissions = permissions,
                navigateToCamera = {
                    navController.navigate(NavScreen.Camera.createRoute(root)) {
                        launchSingleTop = true
                        popUpTo(NavScreen.Permissions.createRoute(root)) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    )
}
private fun NavGraphBuilder.addCameraScreen(
    navController: NavController,
    root: Screen,
    holder: CameraDataHolder,
    executor: Executor,
) {
    composable(
        route = NavScreen.Camera.createRoute(root),
        content = {
            CameraScreen(
                holder = holder,
                executor = executor
            )
        }
    )
}
private fun NavGraphBuilder.addMediaScreen(
    navController: NavController,
    root: Screen,
) {
    composable(
        route = NavScreen.Media.createRoute(root),
        content = {
            MediaScreen()
        }
    )
}