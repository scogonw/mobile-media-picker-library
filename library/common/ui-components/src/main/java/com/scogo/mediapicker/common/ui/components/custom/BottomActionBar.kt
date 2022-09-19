package com.scogo.mediapicker.common.ui.components.custom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.scogo.mediapicker.common.ui_theme.ButtonDimes
import com.scogo.mediapicker.common.ui_theme.Dimens
import com.scogo.mediapicker.common.ui_theme.LightBlue
import com.scogo.mediapicker.common.ui_theme.LightGrey

@Composable
fun BottomActionBar(
    modifier: Modifier,
    header: String,
    actionName: String,
    icon: ImageVector,
    onActionClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .background(color = LightGrey)
            .padding(horizontal = Dimens.Three, vertical = Dimens.One),
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
                    text = header,
                    style = MaterialTheme.typography.caption,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .background(color = Color.LightGray, shape = CircleShape)
                        .size(Dimens.Three),
                    contentAlignment = Alignment.Center,
                    content =
                    {
                        Icon(
                            modifier = Modifier.size(ButtonDimes.Five),
                            imageVector = Icons.Filled.Close,
                            tint = Color.White,
                            contentDescription = null,
                        )
                    }
                )
                Spacer(modifier = Modifier.width(Dimens.One))
                RoundedButton(
                    modifier = Modifier,
                    text = actionName,
                    onActionClick = onActionClick,
                )
            }
        }
    )
}

@Preview
@Composable
private fun Preview() {
    BottomActionBar(
        modifier = Modifier.fillMaxWidth(),
        header = "View",
        actionName = "Add",
        icon = Icons.Default.Image
    )
}