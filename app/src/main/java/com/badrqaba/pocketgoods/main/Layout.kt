package com.badrqaba.pocketgoods.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.badrqaba.authentication_feature.presentation.auth.AuthEvent
import com.badrqaba.authentication_feature.presentation.auth.AuthState
import com.badrqaba.authentication_feature.presentation.auth.login.LoginFormEvent
import com.badrqaba.authentication_feature.presentation.auth.login.LoginState
import com.badrqaba.authentication_feature.presentation.auth.register.RegisterFormEvent
import com.badrqaba.authentication_feature.presentation.auth.register.RegisterFormState
import com.badrqaba.authentication_feature.presentation.auth.toolbar.ToolbarEvent
import com.badrqaba.authentication_feature.presentation.auth.toolbar.ToolbarState
import com.badrqaba.core.domain.model.User
import com.badrqaba.core_ui.component.Footer
import com.badrqaba.core_ui.component.Page404
import com.badrqaba.core_ui.component.snackbar.SnackbarController
import com.badrqaba.core_ui.component.snackbar.SnackbarHost
import com.badrqaba.core_ui.util.PARENT_SCROLLABLE_CONTAINER
import com.badrqaba.core_ui.util.PRODUCT_ID_KEY
import com.badrqaba.core_ui.util.SCAFFOLD_TEST_TAG
import com.badrqaba.core_ui.util.SEARCH_QUERY_KEY
import com.badrqaba.core_ui.util.Screen
import com.badrqaba.core_ui.util.reachedBottom
import com.badrqaba.core_ui.util.screenHeight
import com.badrqaba.pocketgoods.component.HeaderSection
import com.badrqaba.pocketgoods.routes.AppNavHost
import com.badrqaba.pocketgoods.routes.HomeRoute
import com.badrqaba.pocketgoods.routes.ProductDetailRoute
import com.badrqaba.pocketgoods.routes.ProductSearchRoute
import com.badrqaba.pocketgoods.routes.WishlistScreenRoute
import com.badrqaba.product_feature.presentation.screen.ProductViewModel
import com.badrqaba.wishlist_feature.presentation.screen.WishlistManagerViewModel
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun Layout(
    mainActivityState: MainActivityState,
    loginSuccessEvents: SharedFlow<User>,
    authState: AuthState,
    onAuthEvent: (AuthEvent) -> Unit,
    onMainEvent: (MainActivityEvent) -> Unit,
    toolbarState: ToolbarState,
    onToolbarEvent: (ToolbarEvent) -> Unit,
    loginState: LoginState,
    onLoginEvent: (LoginFormEvent) -> Unit,
    registerState: RegisterFormState,
    onRegisterEvent: (RegisterFormEvent) -> Unit,
    snackbarController: SnackbarController,
) {
    val scrollState = rememberScrollState()
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val productViewModel = hiltViewModel<ProductViewModel>()
    val productState by productViewModel.state.collectAsState()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    val reachedBottom: Boolean by remember {
        derivedStateOf { scrollState.reachedBottom(bufferPx = 20) }
    }

    Scaffold(
        modifier = Modifier.testTag(SCAFFOLD_TEST_TAG),
        snackbarHost = {
            SnackbarHost(
                snackbarHostState = snackbarHostState,
                snackbarData = mainActivityState.snackbarData,
                isDarkTheme = mainActivityState.isDarkTheme
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(color = MaterialTheme.colorScheme.background)
                .verticalScroll(state = scrollState)
                .testTag(PARENT_SCROLLABLE_CONTAINER),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                HeaderSection(
                    loginSuccessEvents = loginSuccessEvents,
                    modifier = Modifier.requiredHeight(height = screenHeight() * 0.5f),
                    isDarkTheme = mainActivityState.isDarkTheme,
                    authState = authState,
                    onAuthEvent = onAuthEvent,
                    productState = productState,
                    onProductEvent = productViewModel::onEvent,
                    onMainEvent = onMainEvent,
                    toolbarState = toolbarState,
                    onToolbarEvent = onToolbarEvent,
                    loginState = loginState,
                    onLoginEvent = onLoginEvent,
                    registerState = registerState,
                    onRegisterEvent = onRegisterEvent,
                    navController = navController,
                    snackbarHostState = snackbarHostState,
                    snackbarController = snackbarController,
                    focusManager = focusManager,
                    context = context
                )

                AppNavHost(
                    modifier = Modifier.fillMaxSize(),
                    navController = navController,
                    startDestination = mainActivityState.latestVisitedScreen
                ) {
                    //HomeScreen Route
                    composable(route = Screen.HomeScreen.route) {
                        HomeRoute(
                            authState = authState,
                            onMainEvent = onMainEvent,
                            productState = productState,
                            onProductEvent = productViewModel::onEvent,
                            navController = navController,
                            snackbarHostState = snackbarHostState,
                            snackbarController = snackbarController,
                            context = context,
                            shouldPaginate = reachedBottom
                        )
                    }

                    //ProductDetailScreen Route
                    composable(
                        route = Screen.ProductDetailScreen.route,
                        arguments = listOf(
                            navArgument(PRODUCT_ID_KEY) {
                                type = NavType.LongType
                                nullable = false
                            }
                        )
                    ) { backstackEntry ->
                        val viewModel = hiltViewModel<ProductViewModel>(backstackEntry)
                        val state by viewModel.state.collectAsState()

                        ProductDetailRoute(
                            state = state,
                            onProductEvent = viewModel::onEvent,
                            authState = authState,
                            onMainEvent = onMainEvent,
                            snackbarHostState = snackbarHostState,
                            snackbarController = snackbarController,
                            context = context
                        )
                    }

                    //WishlistManagerScreen Route
                    composable(route = Screen.WishlistManagerScreen.route) {
                        val wishlistManagerViewModel = hiltViewModel<WishlistManagerViewModel>()
                        val wishlistScreenState by wishlistManagerViewModel.state.collectAsState()

                        WishlistScreenRoute(
                            authState = authState,
                            wishlistScreenState = wishlistScreenState,
                            wishlistManagerViewModel = wishlistManagerViewModel,
                            onMainEvent = onMainEvent,
                            navController = navController,
                            snackbarHostState = snackbarHostState,
                            snackbarController = snackbarController,
                            context = context
                        )
                    }

                    //ProductSearchResultScreen Route
                    composable(
                        route = Screen.ProductSearchResultScreen.route,
                        arguments = listOf(
                            navArgument(SEARCH_QUERY_KEY) {
                                type = NavType.StringType
                                nullable = false
                            }
                        )
                    ) { backstackEntry ->
                        val viewModel = hiltViewModel<ProductViewModel>(backstackEntry)
                        val state by viewModel.state.collectAsState()
                        ProductSearchRoute(
                            authState = authState,
                            onMainEvent = onMainEvent,
                            snackbarHostState = snackbarHostState,
                            snackbarController = snackbarController,
                            context = context,
                            navController = navController,
                            shouldPaginate = reachedBottom,
                            productState = state,
                            onProductEvent = viewModel::onEvent
                        )
                    }

                    //Page404 Route
                    composable(route = Screen.UnAuthorizedScreen.route) {
                        Page404(
                            onHomeClick = {
                                navController.navigate(
                                    route = Screen.HomeScreen.route
                                )
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(height = 20.dp))
            }

            Footer(footerHeight = 90.dp)
        }
    }
}