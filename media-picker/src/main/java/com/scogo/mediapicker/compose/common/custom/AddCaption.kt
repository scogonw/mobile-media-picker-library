package com.scogo.mediapicker.compose.common.custom

import androidx.compose.foundation.layout.*
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
import com.scogo.mediapicker.compose.R
import com.scogo.mediapicker.compose.presentation.theme.ButtonDimes
import com.scogo.mediapicker.compose.presentation.theme.Dimens
import com.scogo.mediapicker.compose.presentation.theme.LightBlue

@Composable
internal fun AddCaption(
    modifier: Modifier,
    hasInput: Boolean,
    textFieldState: MutableState<TextFieldValue>,
    onActionClick: () -> Unit
) {
    var text by remember { textFieldState }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.One, vertical = Dimens.Two)
        ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        if(hasInput) {
            OutlinedTextField(
                modifier = Modifier
                    .height(ButtonDimes.Six)
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
        }
        IconRoundedButton(
            modifier = Modifier
                .height(Dimens.Six)
                .padding(start = Dimens.One),
            icon = Icons.Filled.Send,
            onActionClick = onActionClick
        )
    }
}