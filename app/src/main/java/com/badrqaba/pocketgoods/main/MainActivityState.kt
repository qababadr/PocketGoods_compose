package com.badrqaba.pocketgoods.main

import com.badrqaba.core_ui.component.snackbar.SnackbarData
import com.badrqaba.core_ui.util.Screen

data class MainActivityState(
    val isPageLoading: Boolean = false,
    val isDarkTheme: Boolean = false,
    val isSplashScreenVisible: Boolean = true,
    val snackbarData: SnackbarData = SnackbarData(),
    val latestVisitedScreen: String = Screen.HomeScreen.route,
)
