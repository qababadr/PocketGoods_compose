package com.badrqaba.pocketgoods.routes

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost


@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier,
    builder: NavGraphBuilder.() -> Unit
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
        enterTransition = {
            slideInHorizontally(
                animationSpec = tween(700)
            ) { fullWidth -> fullWidth }
        },
        exitTransition = {
            slideOutHorizontally(
                animationSpec = tween(700)
            ) { fullWidth -> -fullWidth }
        },
        popEnterTransition = {
            slideInHorizontally(
                animationSpec = tween(700)
            ) { fullWidth -> -fullWidth }
        },
        popExitTransition = {
            slideOutHorizontally(
                animationSpec = tween(700)
            ) { fullWidth -> fullWidth }
        },
        builder = builder
    )
}