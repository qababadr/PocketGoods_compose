package com.badrqaba.wishlist_feature.presentation.screen

import com.badrqaba.core.domain.model.WishlistItem

sealed class WishlistManagerScreenEvent {
    data class GetWishlistItems(
        val userId: Long
    ): WishlistManagerScreenEvent()

    data class DeleteWishlistItem(
        val userId: Long,
        val onSuccess: (String) -> Unit,
        val onError: (Throwable?, String) -> Unit
    ) : WishlistManagerScreenEvent()

    data class ShowDeleteConfirmationModal(
        val wishlistItem: WishlistItem
    ): WishlistManagerScreenEvent()

    data object CloseDeleteConfirmationModal: WishlistManagerScreenEvent()
}