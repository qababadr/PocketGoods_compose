package com.badrqaba.core_ui.util.media_query

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.badrqaba.core_ui.util.screenWidth

@Composable
fun getScreenSize(): ScreenSize {
    val width = screenWidth()

    return when {
        width >= 840.dp -> ScreenSize.XLARGE
        width >= 600.dp -> ScreenSize.LARGE
        width >= 480.dp -> ScreenSize.MEDIUM
        else -> ScreenSize.SMALL
    }

}