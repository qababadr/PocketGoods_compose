package com.badrqaba.pocketgoods.routes

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
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
import com.badrqaba.product_feature.presentation.screen.ProductEvent
import com.badrqaba.product_feature.presentation.screen.ProductState
import com.badrqaba.product_feature.presentation.screen.SearchResultScreen

@Composable
fun ProductSearchRoute(
    productState: ProductState,
    onProductEvent: (ProductEvent) -> Unit,
    shouldPaginate: Boolean,
    authState: AuthState,
    onMainEvent: (MainActivityEvent) -> Unit,
    snackbarHostState: SnackbarHostState,
    snackbarController: SnackbarController,
    context: Context,
    navController: NavHostController
) {
    SearchResultScreen(
        state = productState,
        shouldPaginate = shouldPaginate,
        authenticatedUser = authState.authenticatedUser,
        onEvent = onProductEvent,
        onToggleWishlist = { productId, _ ->
            if (authState.authenticatedUser != null) {
                onMainEvent(
                    MainActivityEvent.OnSetSnackbarData(
                        data = SnackbarData(
                            snackbarIcon = R.drawable.check_circle,
                            severity = SnackbarSeverity.Success
                        )
                    )
                )
                onMainEvent(
                    MainActivityEvent.OnToggleWishlist(
                        userId = authState.authenticatedUser!!.id,
                        productId = productId,
                        onAdded = {
                            snackbarController.showSnackbar(
                                snackbarHostState = snackbarHostState,
                                message = context.getString(R.string.txt_added_to_wishlist)
                            )
                        },
                        onRemoved = {
                            snackbarController.showSnackbar(
                                snackbarHostState = snackbarHostState,
                                message = context.getString(R.string.txt_removed_from_wishlist)
                            )
                        }
                    ))
            } else {
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
                    message = context.getString(R.string.txt_unauthenticated)
                )
            }
        },
        onViewProduct = { productId ->
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