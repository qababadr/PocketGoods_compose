package com.badrqaba.pocketgoods.main

import com.badrqaba.core.domain.model.User
import com.badrqaba.core_ui.component.snackbar.SnackbarData

sealed class MainActivityEvent {
    data class OnLoadData(
        val onSuccess: (User?) -> Unit,
        val onError: () -> Unit,
        val onStartLoading: (() -> Unit)? = null,
        val onDoneLoading: (() -> Unit)? = null
    ): MainActivityEvent()

    data class OnSetDarkTheme(
        val isDarkTheme: Boolean
    ): MainActivityEvent()

    data class OnToggleWishlist(
        val userId: Long,
        val productId: Long,
        val onAdded: () -> Unit,
        val onRemoved: () -> Unit
    ): MainActivityEvent()

    data class OnSetSnackbarData(
        val data: SnackbarData
    ) : MainActivityEvent()

    data class OnSetCurrentDestination(
        val currentDestination: String
    ): MainActivityEvent()
}