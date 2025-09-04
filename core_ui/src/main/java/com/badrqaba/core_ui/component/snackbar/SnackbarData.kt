package com.badrqaba.core_ui.component.snackbar

import androidx.annotation.DrawableRes

data class SnackbarData(
    @DrawableRes
    val snackbarIcon: Int = 0,
    val severity: SnackbarSeverity = SnackbarSeverity.Info
)
