package com.scogo.mediapicker.compose.permission

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionsScreen(
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    permissions: List<String>,
    navigateToHome: () -> Unit,
) {
    val permissionsState = rememberMultiplePermissionsState(permissions = permissions)
    if(permissionsState.allPermissionsGranted) {
        navigateToHome()
    }else {
        PermissionsView(
            modifier = modifier,
            scaffoldState = scaffoldState,
            navigateToHome = navigateToHome
        )
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
internal fun PermissionsView(
    modifier: Modifier,
    scaffoldState: ScaffoldState,
    navigateToHome: () -> Unit,
){
    val permissionsDenied = remember {
        mutableStateOf(false)
    }
    Scaffold(
        scaffoldState = scaffoldState,
        content = {
            Column(
                modifier = modifier.fillMaxSize()
            ) {

            }
        }
    )
}

@Preview
@Composable
 fun PermissionsPreview() {
    Button(onClick = { /*TODO*/ }) {

    }
}











