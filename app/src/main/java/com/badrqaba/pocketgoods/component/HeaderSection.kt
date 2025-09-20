package com.badrqaba.pocketgoods.component

import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.badrqaba.authentication_feature.presentation.auth.AuthEvent
import com.badrqaba.authentication_feature.presentation.auth.AuthState
import com.badrqaba.authentication_feature.presentation.auth.login.LoginFormEvent
import com.badrqaba.authentication_feature.presentation.auth.login.LoginState
import com.badrqaba.authentication_feature.presentation.auth.register.RegisterFormEvent
import com.badrqaba.authentication_feature.presentation.auth.register.RegisterFormState
import com.badrqaba.authentication_feature.presentation.auth.toolbar.Toolbar
import com.badrqaba.authentication_feature.presentation.auth.toolbar.ToolbarEvent
import com.badrqaba.authentication_feature.presentation.auth.toolbar.ToolbarState
import com.badrqaba.core.domain.model.User
import com.badrqaba.core_ui.R
import com.badrqaba.core_ui.component.Header
import com.badrqaba.core_ui.component.snackbar.SnackbarController
import com.badrqaba.core_ui.component.snackbar.SnackbarData
import com.badrqaba.core_ui.component.snackbar.SnackbarSeverity
import com.badrqaba.core_ui.util.PRODUCT_ID_KEY
import com.badrqaba.core_ui.util.SEARCH_QUERY_KEY
import com.badrqaba.core_ui.util.Screen
import com.badrqaba.core_ui.util.navigateTo
import com.badrqaba.pocketgoods.main.MainActivityEvent
import com.badrqaba.product_feature.presentation.component.SearchInput
import com.badrqaba.product_feature.presentation.screen.ProductEvent
import com.badrqaba.product_feature.presentation.screen.ProductState
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun HeaderSection(
    loginSuccessEvents: SharedFlow<User>,
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onMainEvent: (MainActivityEvent) -> Unit,
    productState: ProductState,
    onProductEvent: (ProductEvent) -> Unit,
    focusManager: FocusManager,
    authState: AuthState,
    onAuthEvent: (AuthEvent) -> Unit,
    isDarkTheme: Boolean,
    toolbarState: ToolbarState,
    onToolbarEvent: (ToolbarEvent) -> Unit,
    loginState: LoginState,
    onLoginEvent: (LoginFormEvent) -> Unit,
    registerState: RegisterFormState,
    onRegisterEvent: (RegisterFormEvent) -> Unit,
    snackbarController: SnackbarController,
    snackbarHostState: SnackbarHostState,
    context: Context,
) {
    Header(
        modifier = modifier,
        onLogoClick = {
            navController.navigateTo(
                screen = Screen.HomeScreen,
                onBeforeNavigate = { route ->
                    onMainEvent(
                        MainActivityEvent.OnSetCurrentDestination(currentDestination = route)
                    )
                }
            )
        },
        searchInput = {
            SearchInput(
                modifier = Modifier.padding(top = 24.dp, start = 8.dp),
                isProcessing = productState.isSearching,
                query = productState.searchQuery,
                searchLabel = stringResource(id = R.string.lbl_search),
                onSearch = {
                    onProductEvent(ProductEvent.OnQueryChange(query = it))
                },
                search = {
                    navController.navigateTo(
                        screen = Screen.ProductSearchResultScreen,
                        SEARCH_QUERY_KEY to productState.searchQuery,
                        onBeforeNavigate = { route ->
                            onMainEvent(
                                MainActivityEvent
                                    .OnSetCurrentDestination(currentDestination = route)
                            )
                        }
                    )
                },
                onClearQuery = {
                    onProductEvent(ProductEvent.ClearSearch)
                },
                onSuggestedProductClick = { productId ->
                    navController.navigateTo(
                        screen = Screen.ProductDetailScreen,
                        PRODUCT_ID_KEY to productId,
                        onBeforeNavigate = { route ->
                            onMainEvent(
                                MainActivityEvent
                                    .OnSetCurrentDestination(currentDestination = route)
                            )
                        }
                    )
                },
                focusManager = focusManager,
                closeButtonContentDescription = stringResource(id = R.string.cd_clear_search),
                textFieldContentDescription = stringResource(id = R.string.cd_search_products),
                searchButtonContentDescription = stringResource(R.string.cd_search_input_search_button),
                suggestedProducts = productState.suggestedProducts
            )
        },
        userMenu = {
            Toolbar(
                loginSuccessEvents = loginSuccessEvents,
                authState = authState,
                isDarkTheme = isDarkTheme,
                toolbarState = toolbarState,
                onToolbarEvent = onToolbarEvent,
                loginState = loginState,
                onLoginFormEvent = onLoginEvent,
                registerState = registerState,
                onRegisterFormEvent = onRegisterEvent,
                onLogout = {
                    authState.authenticatedUser?.let { user ->
                        onAuthEvent(
                            AuthEvent.OnLogout(
                                userId = user.id,
                                onLoggedOut = {
                                    onMainEvent(
                                        MainActivityEvent.OnSetSnackbarData(
                                            data = SnackbarData(
                                                snackbarIcon = R.drawable.check_circle,
                                                severity = SnackbarSeverity.Success
                                            )
                                        )

                                    )
                                    snackbarController.showSnackbar(
                                        snackbarHostState = snackbarHostState,
                                        message = context.getString(R.string.txt_logged_out)
                                    )
                                },
                                onError = {
                                    onMainEvent(
                                        MainActivityEvent.OnSetSnackbarData(
                                            data = SnackbarData(
                                                snackbarIcon = R.drawable.close_circle,
                                                severity = SnackbarSeverity.Error
                                            )
                                        )
                                    )
                                    snackbarController.showSnackbar(
                                        snackbarHostState = snackbarHostState,
                                        message = context.getString(R.string.err_logged_out)
                                    )
                                }
                            )
                        )
                    }
                },
                onLoggedIn = { user ->
                    onLoginEvent(
                        LoginFormEvent.SetAlert(
                            message = "",
                            visible = false
                        )
                    )
                    onToolbarEvent(ToolbarEvent.CloseModal)
                    onMainEvent(
                        MainActivityEvent.OnSetSnackbarData(
                            data = SnackbarData(
                                snackbarIcon = R.drawable.check_circle,
                                severity = SnackbarSeverity.Success
                            )
                        )
                    )
                    snackbarController.showSnackbar(
                        snackbarHostState = snackbarHostState,
                        message = context.getString(R.string.txt_logged_in, user.name)
                    )
                },
                onUserMenuClick = {
                    onAuthEvent(
                        AuthEvent.OnRefreshUserData(
                            onRefreshFailed = {
                                onMainEvent(
                                    MainActivityEvent.OnSetSnackbarData(
                                        data = SnackbarData(
                                            snackbarIcon = R.drawable.close_circle,
                                            severity = SnackbarSeverity.Error
                                        )
                                    )
                                )
                                snackbarController.showSnackbar(
                                    snackbarHostState = snackbarHostState,
                                    message = context.getString(R.string.err_refresh_user_data)
                                )
                            },
                        )
                    )
                },
                onWishlistClick = {
                    if(authState.authenticatedUser == null) {
                        navController.navigateTo(
                            screen = Screen.UnAuthorizedScreen,
                        )
                    } else {
                        navController.navigateTo(
                            screen = Screen.WishlistManagerScreen,
                            onBeforeNavigate = {
                                onMainEvent(
                                    MainActivityEvent.OnSetCurrentDestination(
                                        currentDestination = it
                                    )
                                )
                            }
                        )
                    }
                },
                onDarkThemeSwitched = { isDarkTheme ->
                    onMainEvent(
                        MainActivityEvent.OnSetDarkTheme(
                            isDarkTheme = isDarkTheme
                        )
                    )
                }
            )
        }
    )
}