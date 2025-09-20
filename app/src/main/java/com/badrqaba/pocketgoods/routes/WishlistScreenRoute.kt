package com.badrqaba.pocketgoods.routes

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.badrqaba.authentication_feature.presentation.auth.AuthState
import com.badrqaba.core_ui.R
import com.badrqaba.core_ui.component.snackbar.SnackbarController
import com.badrqaba.core_ui.component.snackbar.SnackbarData
import com.badrqaba.core_ui.component.snackbar.SnackbarSeverity
import com.badrqaba.core_ui.util.PRODUCT_ID_KEY
import com.badrqaba.core_ui.util.Screen
import com.badrqaba.core_ui.util.navigateTo
import com.badrqaba.pocketgoods.main.MainActivityEvent
import com.badrqaba.wishlist_feature.presentation.screen.WishlistManagerScreen
import com.badrqaba.wishlist_feature.presentation.screen.WishlistManagerScreenEvent
import com.badrqaba.wishlist_feature.presentation.screen.WishlistManagerState
import com.badrqaba.wishlist_feature.presentation.screen.WishlistManagerViewModel

@Composable
fun WishlistScreenRoute(
    authState: AuthState,
    wishlistScreenState: WishlistManagerState,
    wishlistManagerViewModel: WishlistManagerViewModel,
    onMainEvent: (MainActivityEvent) -> Unit,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    snackbarController: SnackbarController,
    context: Context
) {
    WishlistManagerScreen(
        authenticatedUser = authState.authenticatedUser,
        state = wishlistScreenState,
        onEvent = { wishlistManagerViewModel.onEvent(event = it) },
        onDeleteSuccess = { productTitle ->
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
                message = context.getString(
                    R.string.txt_removed_from_wishlist,
                    productTitle
                )
            )
        },
        onDeleteError = { productTitle ->
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
                message = context.getString(
                    R.string.err_remove_from_wishlist,
                    productTitle
                )
            )
        },
        onUnAuthorized = {
            navController.navigate(
                route = Screen.UnAuthorizedScreen.route
            )
        },
        onProductClick = { productId ->
            navController.navigateTo(
                screen = Screen.ProductDetailScreen,
                PRODUCT_ID_KEY to productId,
                onBeforeNavigate = { route ->
                    onMainEvent(
                        MainActivityEvent.OnSetCurrentDestination(
                            currentDestination = route
                        )
                    )
                }
            )
        }
    )
}