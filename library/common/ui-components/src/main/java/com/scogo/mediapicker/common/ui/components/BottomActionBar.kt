package com.scogo.mediapicker.common.ui.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.scogo.mediapicker.common.ui_res.R
import com.scogo.mediapicker.common.ui_theme.Dimens
import com.scogo.mediapicker.common.ui_theme.LightGrey

@Composable
fun BottomActionBar(
    modifier: Modifier,
    @StringRes title: Int,
    @DrawableRes icon: Int,
    @StringRes action: Int,
    onActionClick: () -> Unit = {}
) {
    Row(
        modifier = modifier.background(color = LightGrey),
        content = {
            Icon(
                modifier = Modifier.size(Dimens.Three),
                painter = painterResource(id = icon),
                contentDescription = null
            )
            Text(
                text = stringResource(id = title),
                style = MaterialTheme.typography.body1
            )
            Button(
                onClick = {

            }) {

            }
        }
    )
}

@Preview
@Composable
private fun Preview() {
    BottomActionBar(
        modifier = Modifier,
        title = R.string.app_name,
        icon = R.drawable.arrow_down,
        action = R.string.app_name
    )
}