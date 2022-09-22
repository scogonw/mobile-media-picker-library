package com.scogo.mediapicker.compose.presentation.permission

import android.annotation.SuppressLint
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.scogo.mediapicker.compose.R
import com.scogo.mediapicker.compose.presentation.theme.ButtonDimes
import com.scogo.mediapicker.compose.presentation.theme.Dimens
import com.scogo.mediapicker.compose.presentation.theme.ScogoTheme

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun PermissionsScreen(
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    permissions: List<String>,
    navigateToCamera: () -> Unit,
) {
    val permissionsState = rememberMultiplePermissionsState(permissions = permissions)
    val permissionsDenied = remember { mutableStateOf(false) }

    if(permissionsState.allPermissionsGranted) {
        navigateToCamera()
    }else {
        PermissionsView(
            modifier = modifier,
            scaffoldState = scaffoldState,
            launchPermissions = {
                permissionsState.launchMultiplePermissionRequest()
            }
        )
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
internal fun PermissionsView(
    modifier: Modifier,
    scaffoldState: ScaffoldState,
    launchPermissions: () -> Unit
){
    Scaffold(
        scaffoldState = scaffoldState,
        content = {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = Dimens.Three)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(2F),
                    painter = painterResource(id = R.drawable.image_wall),
                    contentScale = ContentScale.Inside,
                    contentDescription = null,
                )
                Spacer(modifier = Modifier.height(Dimens.Two))
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = stringResource(id = R.string.enable_permissions).uppercase(),
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.heightIn(Dimens.Three))
                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = CircleShape,
                    onClick = launchPermissions,
                    contentPadding = PaddingValues(ButtonDimes.Three),
                    content = {
                        Text(
                            text = stringResource(id = R.string.grant_permissions)
                        )
                    }
                )
            }
        }
    )
}

@Preview(uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun PermissionsPreview() {
    ScogoTheme {
        PermissionsView(
            modifier = Modifier,
            scaffoldState = rememberScaffoldState(),
            launchPermissions = {}
        )
    }
}











