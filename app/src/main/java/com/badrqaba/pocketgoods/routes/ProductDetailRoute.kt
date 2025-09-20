package com.badrqaba.pocketgoods.routes

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.badrqaba.authentication_feature.presentation.auth.AuthState
import com.badrqaba.core_ui.R
import com.badrqaba.core_ui.component.snackbar.SnackbarController
import com.badrqaba.core_ui.component.snackbar.SnackbarData
import com.badrqaba.core_ui.component.snackbar.SnackbarSeverity
import com.badrqaba.pocketgoods.main.MainActivityEvent
import com.badrqaba.product_feature.presentation.screen.ProductDetailScreen
import com.badrqaba.product_feature.presentation.screen.ProductEvent
import com.badrqaba.product_feature.presentation.screen.ProductState

@Composable
fun ProductDetailRoute(
    state: ProductState,
    onProductEvent: (ProductEvent) -> Unit,
    authState: AuthState,
    onMainEvent: (MainActivityEvent) -> Unit,
    snackbarHostState: SnackbarHostState,
    snackbarController: SnackbarController,
    context: Context
) {
    ProductDetailScreen(
        state = state,
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
                                message = context.getString(R.string.txt_removed_from_wishlist, "")
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
        onEvent = onProductEvent,
        authenticatedUser = authState.authenticatedUser
    )
}