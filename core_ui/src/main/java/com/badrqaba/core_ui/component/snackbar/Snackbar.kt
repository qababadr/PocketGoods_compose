package com.badrqaba.core_ui.component.snackbar

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle

@Composable
fun Snackbar(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    actionLabelTextStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    shape: Shape = MaterialTheme.shapes.medium,
    actionOnNewLine: Boolean = false,
    severity: SnackbarSeverity = SnackbarSeverity.Info,
    isDarkTheme: Boolean,
    content: @Composable (String, SnackbarSeverity) -> Unit
) {
    SnackbarTheme(
        severity = severity,
        darkTheme = isDarkTheme
    ) {
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = modifier
        ) { data ->
            Snackbar(
                shape = shape,
                containerColor = MaterialTheme.colorScheme.background,
                actionOnNewLine = actionOnNewLine,
                action = {
                    data.visuals.actionLabel?.let { actionLabel ->
                        TextButton(onClick = { data.performAction() }) {
                            Text(
                                text = actionLabel,
                                style = actionLabelTextStyle,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
            ) {
                content(data.visuals.message, severity)
            }
        }
    }
}