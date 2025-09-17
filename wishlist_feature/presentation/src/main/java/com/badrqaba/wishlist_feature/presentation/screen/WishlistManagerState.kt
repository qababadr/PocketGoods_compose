package com.badrqaba.wishlist_feature.presentation.screen

import com.badrqaba.core.domain.model.WishlistItem

data class WishlistManagerState(
    val isPageLoading: Boolean = true,
    val isDeleting: Boolean = false,
    val wishlist: List<WishlistItem> = emptyList(),
    val selectedWishlistItem: WishlistItem? = null,
    val error: Throwable? = null
)
