package com.badrqaba.core_ui.theme

import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView

private val darkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = onPrimaryDark,
    secondary = SecondaryDark,
    error = RedErrorLight,
    background = backgroundDark,
    onBackground = OnBackgroundDark,
    surface = SurfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSecondary = onSurfaceDark,
)

private val lightColorScheme = lightColorScheme(
    primary = PrimaryLight,
    onPrimary = onPrimaryLight,
    secondary = Secondary,
    onSecondary = onSurfaceLight,
    error = RedErrorDark,
    onError = RedErrorLight,
    background = backgroundLight,
    onBackground = OnBackgroundLight,
    surface = SurfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = SurfaceVariantLight
)

@Composable
fun PocketGoodsTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (isDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        isDarkTheme -> darkColorScheme
        else -> lightColorScheme
    }

    val view = LocalView.current
    val context = LocalContext.current
    val activity = context as? ComponentActivity

    if (!view.isInEditMode && activity != null) {
        DisposableEffect(isDarkTheme) {
            activity.enableEdgeToEdge(
                statusBarStyle = if (!isDarkTheme) {
                    SystemBarStyle.light(Color.Transparent.toArgb(), Color.Transparent.toArgb())
                } else {
                    SystemBarStyle.dark(Color.Transparent.toArgb())
                },
                navigationBarStyle = if (!isDarkTheme) {
                    SystemBarStyle.light(Color.Transparent.toArgb(), Color.Transparent.toArgb())
                } else {
                    SystemBarStyle.dark(Color.Transparent.toArgb())
                }
            )

            onDispose { }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = LatoTypography,
        shapes = Shapes,
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background)
            ) {
                content()
            }
        }
    )
}
