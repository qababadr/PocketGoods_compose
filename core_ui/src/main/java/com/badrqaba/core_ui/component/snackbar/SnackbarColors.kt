package com.badrqaba.core_ui.component.snackbar

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val errorOnBackgroundLight = Color(0xFF842029)
val errorOnBackgroundDark = Color(0xFFEA868F)
val errorBackgroundLight = Color(0xFFF8D7DA)
val errorBackgroundDark = Color(0xFF2C0B0E)

val successOnBackgroundLight = Color(0xFF0F5132)
val successOnBackgroundDark = Color(0xFF75B798)
val successBackgroundLight = Color(0xFFD1E7DD)
val successBackgroundDark = Color(0xFF051B11)

val infoOnBackgroundLight = Color(0xFF084298)
val infoOnBackgroundDark = Color(0xFF6EA8FE)
val infoBackgroundLight = Color(0xFFCFE2FF)
val infoBackgroundDark = Color(0xFF031633)

val warningOnBackgroundLight = Color(0xFF664D03)
val warningOnBackgroundDark = Color(0xFFFFDA6A)
val warningBackgroundLight = Color(0xFFFFD54F)
val warningBackgroundDark = Color(0xFF332701)

@Composable
fun snackbarIconColor(severity: SnackbarSeverity): Color {
    return if (isSystemInDarkTheme()) {
        when(severity) {
            SnackbarSeverity.Success -> successOnBackgroundDark
            SnackbarSeverity.Error -> errorOnBackgroundDark
            SnackbarSeverity.Info -> infoOnBackgroundDark
            SnackbarSeverity.Warning -> warningOnBackgroundDark
        }
    } else {
        when(severity) {
            SnackbarSeverity.Success -> successOnBackgroundLight
            SnackbarSeverity.Error -> errorOnBackgroundLight
            SnackbarSeverity.Info -> infoOnBackgroundLight
            SnackbarSeverity.Warning -> warningOnBackgroundLight
        }
    }
}

fun lightColorScheme(severity: SnackbarSeverity): ColorScheme {
    return when (severity) {
        SnackbarSeverity.Error -> lightColorScheme(
            background = errorBackgroundLight,
            onBackground = errorOnBackgroundLight
        )

        SnackbarSeverity.Info -> lightColorScheme(
            background = infoBackgroundLight,
            onBackground = infoOnBackgroundLight
        )

        SnackbarSeverity.Success -> lightColorScheme(
            background = successBackgroundLight,
            onBackground = successOnBackgroundLight
        )

        SnackbarSeverity.Warning -> lightColorScheme(
            background = warningBackgroundLight,
            onBackground = warningOnBackgroundLight
        )
    }
}

fun darkColorScheme(severity: SnackbarSeverity): ColorScheme {
    return when (severity) {
        SnackbarSeverity.Error -> darkColorScheme(
            background = errorBackgroundDark,
            onBackground = errorOnBackgroundDark
        )

        SnackbarSeverity.Info -> darkColorScheme(
            background = infoBackgroundDark,
            onBackground = infoOnBackgroundDark
        )

        SnackbarSeverity.Success -> darkColorScheme(
            background = successBackgroundDark,
            onBackground = successOnBackgroundDark
        )

        SnackbarSeverity.Warning -> darkColorScheme(
            background = warningBackgroundDark,
            onBackground = warningOnBackgroundDark
        )
    }
}