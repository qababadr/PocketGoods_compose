package com.badrqaba.core_ui.component.snackbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.badrqaba.core_ui.R
import com.badrqaba.core_ui.component.ThemeSwitch
import com.badrqaba.core_ui.theme.PocketGoodsTheme
import com.badrqaba.core_ui.util.SCAFFOLD_TEST_TAG
import com.badrqaba.core_ui.util.errorColor
import kotlinx.coroutines.launch

@Composable
fun SnackbarHost(
    snackbarHostState: SnackbarHostState,
    snackbarData: SnackbarData,
    isDarkTheme: Boolean
) {
    Snackbar(
        snackbarHostState = snackbarHostState,
        shape = MaterialTheme.shapes.small,
        modifier = Modifier.padding(
            horizontal = 8.dp,
            vertical = 2.dp
        ),
        severity = snackbarData.severity,
        isDarkTheme = isDarkTheme
    ) { message, severity ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(snackbarData.snackbarIcon),
                contentDescription = "",
                tint = snackbarIconColor(severity, isDarkTheme),
                modifier = Modifier
                    .padding(end = 8.dp)
                    .requiredSize(24.dp)
            )

            Text(
                text = message,
                style = MaterialTheme.typography.labelLarge,
                color = snackbarIconColor(severity, isDarkTheme)
            )
        }
    }
}

@Preview
@Composable
fun SnackbarPreview() {
    Box {
        val snackbarHostState = remember { SnackbarHostState() }
        var snackbarData by remember { mutableStateOf(SnackbarData()) }
        var isDarkTheme by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()
        PocketGoodsTheme(isDarkTheme = isDarkTheme) {
            Scaffold(
                modifier = Modifier.testTag(SCAFFOLD_TEST_TAG),
                snackbarHost = {
                    SnackbarHost(
                        snackbarHostState = snackbarHostState,
                        snackbarData = snackbarData,
                        isDarkTheme = isDarkTheme
                    )
                },
                contentWindowInsets = WindowInsets(0, 0, 0, 0)
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        ThemeSwitch(
                            isDarkTheme = isDarkTheme,
                            onThemeChange = {
                                isDarkTheme = it
                            }
                        )

                        Button(onClick = {
                            snackbarData = snackbarData.copy(
                                snackbarIcon = R.drawable.check_circle,
                                severity = SnackbarSeverity.Success
                            )

                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "Success Snackbar message",
                                    withDismissAction = false
                                )
                            }

                        }) {
                            Text("Success Snackbar")
                        }

                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.errorColor()
                            ),
                            onClick = {
                                snackbarData = snackbarData.copy(
                                    snackbarIcon = R.drawable.close_circle,
                                    severity = SnackbarSeverity.Error
                                )

                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = "Error Snackbar message",
                                        withDismissAction = false
                                    )
                                }

                            }) {
                            Text("Error Snackbar")
                        }
                    }
                }
            }
        }
    }
}