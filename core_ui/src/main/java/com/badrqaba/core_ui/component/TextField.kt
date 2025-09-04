package com.badrqaba.core_ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.badrqaba.core_ui.R
import com.badrqaba.core_ui.theme.PocketGoodsTheme

@Composable
fun TextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    @DrawableRes leadingIcon: Int,
    isError: Boolean,
    textError: String,
    onTrailingIconClick: (() -> Unit) = {},
    @DrawableRes trailingIcon: Int? = null,
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            visualTransformation = visualTransformation,
            placeholder = {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall
                )
            },
            label = {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall
                )
            },
            textStyle = MaterialTheme.typography.bodySmall,
            singleLine = true,
            shape = MaterialTheme.shapes.medium,
            keyboardOptions = keyboardOptions,
            leadingIcon = {
                Icon(
                    painter = painterResource(id = leadingIcon),
                    contentDescription = "leading icon for $label"
                )
            },
            trailingIcon = {
                trailingIcon?.let {
                    Icon(
                        painter = painterResource(id = it),
                        contentDescription = "trailing icon for $label",
                        modifier = Modifier.clickable(onClick = onTrailingIconClick)
                    )
                }
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
            ),
            modifier = modifier.fillMaxWidth(),
            isError = isError
        )

        if (isError) {
            Text(
                text = textError,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.error
            )
        }

    }
}

@Preview
@Composable
private fun TextFieldPreview() {
    PocketGoodsTheme {
        var value by remember { mutableStateOf("") }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            TextField(
                value = value,
                onValueChange = { value = it },
                label = stringResource(id = R.string.lbl_first_and_last_name),
                leadingIcon = R.drawable.tag,
                isError = value.isEmpty(),
                textError = "Provide a non empty value",
            )
        }
    }
}