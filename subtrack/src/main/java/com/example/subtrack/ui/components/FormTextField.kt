package com.example.subtrack.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.subtrack.ui.theme.CardBorder
import com.example.subtrack.ui.theme.TextGray
import com.example.subtrack.ui.theme.TextHint

@Composable
fun FormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    minLines: Int = 1,
    trailingIcon: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    val shape = RoundedCornerShape(20.dp)

    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.border(1.dp, CardBorder, shape),
        readOnly = readOnly,
        label = { Text(text = label) },
        placeholder = placeholder?.let { { Text(text = it) } },
        trailingIcon = trailingIcon,
        suffix = suffix,
        keyboardOptions = keyboardOptions,
        singleLine = singleLine,
        minLines = minLines,
        shape = shape,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedLabelColor = TextGray,
            unfocusedLabelColor = TextGray,
            focusedPlaceholderColor = TextHint,
            unfocusedPlaceholderColor = TextHint
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun FormTextFieldPreview() {
    PreviewWrapper {
        FormTextField(
            value = "Netflix",
            onValueChange = {},
            label = "Название сервиса"
        )
    }
}
