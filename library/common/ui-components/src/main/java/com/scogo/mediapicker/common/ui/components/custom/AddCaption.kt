package com.scogo.mediapicker.common.ui.components.custom

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import com.scogo.mediapicker.common.ui_res.R
import com.scogo.mediapicker.common.ui_theme.ButtonDimes
import com.scogo.mediapicker.common.ui_theme.Dimens
import com.scogo.mediapicker.common.ui_theme.LightBlue

@Composable
fun AddCaption(
    modifier: Modifier,
    textFieldState: MutableState<TextFieldValue>,
    onActionClick: () -> Unit
) {
    var text by remember { textFieldState }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.One, vertical = Dimens.Two),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            modifier = Modifier
                //.height(ButtonDimes.Six)
                .weight(1f)
                .padding(Dimens.Zero),
            value = text,
            onValueChange = {
                text = it
            },
            placeholder = {
                Text(
                    text = stringResource(R.string.enter_your_caption),
                    style = MaterialTheme.typography.subtitle2,
                )
            },
            shape = RoundedCornerShape(Dimens.Four),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = Color.White,
                focusedBorderColor = LightBlue
            ),
        )
        IconRoundedButton(
            modifier = Modifier
                .height(Dimens.Six)
                .padding(start = Dimens.One),
            icon = Icons.Filled.Send,
            onActionClick = onActionClick
        )
    }
}