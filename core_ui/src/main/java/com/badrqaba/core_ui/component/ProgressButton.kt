package com.badrqaba.core_ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.badrqaba.core_ui.theme.PocketGoodsTheme

@Composable
fun ProgressButton(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    shape: Shape = ButtonDefaults.shape,
    buttonColors: ButtonColors = ButtonDefaults.buttonColors(),
    onClick: () -> Unit,
    content: @Composable (Color) -> Unit
) {
    Button(
        modifier = modifier,
        colors = buttonColors,
        shape = shape,
        onClick = { if (!isLoading) onClick() },
        enabled = !isLoading
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = buttonColors.contentColor,
                modifier = Modifier.size(25.dp),
                strokeWidth = 2.dp
            )

            Spacer(modifier = modifier.size(6.dp))
        }

        content(buttonColors.contentColor)
    }
}

@Preview
@Composable
private fun ProgressButtonPreview() {
    PocketGoodsTheme {
        var isLoading by remember { mutableStateOf(false) }

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            ProgressButton(
                isLoading = isLoading,
                modifier = Modifier.wrapContentWidth(align = Alignment.CenterHorizontally),
                shape = MaterialTheme.shapes.small,
                buttonColors = ButtonDefaults.buttonColors().copy(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                ),
                onClick = { isLoading = !isLoading }
            ) {
                Text(
                    text = "Login",
                    color = it,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}