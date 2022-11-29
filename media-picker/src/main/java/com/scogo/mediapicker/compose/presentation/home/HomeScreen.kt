package com.scogo.mediapicker.compose.presentation.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.scogo.mediapicker.compose.presentation.navigation.AppNavigation
import com.scogo.mediapicker.compose.presentation.navigation.AppNavigationParams

@Composable
internal fun HomeScreen(
    params: AppNavigationParams
){
    val controller = rememberNavController()
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        content = { innerPadding ->
            AppNavigation(
                navController = controller,
                modifier = Modifier.padding(innerPadding),
                params = params
            )
        }
    )
}