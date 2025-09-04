package com.badrqaba.core_ui.util

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.ui.platform.LocalResources
import com.badrqaba.core_ui.theme.ErrorDark
import com.badrqaba.core_ui.theme.ErrorLight
import com.badrqaba.core_ui.theme.OnWarningDark
import com.badrqaba.core_ui.theme.OnWarningLight
import com.badrqaba.core_ui.theme.SuccessDark
import com.badrqaba.core_ui.theme.SuccessLight
import com.badrqaba.core_ui.theme.WarningDark
import com.badrqaba.core_ui.theme.WarningLight

inline fun NavHostController.navigateTo(
    screen: Screen,
    vararg args: Pair<String, Any?> = emptyArray(),
    onBeforeNavigate: (String) -> Unit = {}
) {
    val route = screen.path(*args)
    onBeforeNavigate(route)
    navigate(route)
}

@Composable
fun screenWidth(): Dp {
    return LocalResources.current.displayMetrics.widthPixels.dp /
            LocalDensity.current.density
}

@Composable
fun screenHeight(): Dp {
    return LocalResources.current.displayMetrics.heightPixels.dp /
            LocalDensity.current.density
}

@Composable
fun MaterialTheme.successColor(): Color {
    return if (isSystemInDarkTheme()) SuccessDark else SuccessLight
}

@Composable
fun MaterialTheme.warningColor(): Color {
    return if (isSystemInDarkTheme()) WarningDark else WarningLight
}

@Composable
fun MaterialTheme.errorColor(): Color {
    return if (isSystemInDarkTheme()) ErrorDark else ErrorLight
}

@Composable
fun MaterialTheme.onWarningColor(): Color {
    return if (isSystemInDarkTheme()) OnWarningDark else OnWarningLight
}

fun ScrollState.reachedBottom(bufferPx: Int = 0): Boolean {
    return (value >= maxValue - bufferPx)
}


