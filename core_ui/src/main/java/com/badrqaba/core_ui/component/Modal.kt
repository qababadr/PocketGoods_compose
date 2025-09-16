package com.badrqaba.core_ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.badrqaba.core_ui.theme.PocketGoodsTheme
import com.badrqaba.core_ui.util.onWarningColor
import com.badrqaba.core_ui.util.warningColor

@Composable
fun Modal(
    isOpen: Boolean,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    properties: DialogProperties = DialogProperties(),
    headerColor: Color = MaterialTheme.colorScheme.primary,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    shape: Shape = MaterialTheme.shapes.medium,
    onDismiss: (() -> Unit)? = null,
    closeIcon: @Composable () -> Unit = {
        Icon(
            Icons.Default.Close,
            contentDescription = "Close",
            tint = Color.White
        )
    },
    content: @Composable () -> Unit
) {
    if (isOpen) {
        Dialog(
            onDismissRequest = { onDismiss?.invoke() },
            properties = properties
        ) {
            Surface(
                color = backgroundColor,
                shape = shape,
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 8.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = modifier
                            .requiredHeight(40.dp)
                            .fillMaxWidth()
                            .background(color = headerColor),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        title()

                        IconButton(onClick = { onDismiss?.invoke() }) {
                            closeIcon()
                        }
                    }
                    content()
                }
            }
        }
    }
}

@Preview()
@Composable
private fun ModalPreview() {

    var isOpen by remember { mutableStateOf(false) }

    PocketGoodsTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {

            Button(onClick = {
                isOpen = true
            }) {
                Text(text = "Open modal")
            }

            Spacer(modifier = Modifier.padding(all = 20.dp))

            Modal(
                isOpen = isOpen,
                headerColor = MaterialTheme.warningColor(),
                closeIcon = {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.onWarningColor()
                    )
                },
                title = {
                    Text(
                        text = "Confirmation",
                        color = MaterialTheme.onWarningColor(),
                        modifier = Modifier.padding(horizontal = 12.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                onDismiss = { isOpen = false },
                backgroundColor = MaterialTheme.colorScheme.surface
            ) {
                Text(
                    text = "Are you sure you want to proceed",
                    modifier = Modifier.padding(all = 12.dp),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}