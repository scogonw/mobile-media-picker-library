package com.scogo.mediapicker.common.ui.components.custom

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.scogo.mediapicker.common.ui_theme.Dimens
import com.scogo.mediapicker.common.ui_theme.LightGrey

@Composable
fun BottomActionBar(
    modifier: Modifier,
    @StringRes title: Int,
    icon: ImageVector,
    @StringRes actionName: Int,
    onActionClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = LightGrey)
            .padding(horizontal = Dimens.Three, vertical = Dimens.One)
        ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        content = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    modifier = Modifier.size(Dimens.Two),
                    imageVector = icon,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(Dimens.One))
                Text(
                    modifier = Modifier,
                    text = stringResource(id = title),
                    style = MaterialTheme.typography.caption,
                    fontWeight = FontWeight.Bold
                )
            }
            RoundedButton(
                modifier = Modifier,
                actionName = actionName,
                onActionClick = onActionClick,
            )
        }
    )
}