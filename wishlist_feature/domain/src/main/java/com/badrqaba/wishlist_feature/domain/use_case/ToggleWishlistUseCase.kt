package com.badrqaba.wishlist_feature.domain.use_case

import com.badrqaba.wishlist_feature.domain.repository.WishlistRepository

class ToggleWishlistUseCase(private val repository: WishlistRepository) {

    suspend operator fun invoke(userId: Long, productId: Long): Long {
        return repository.toggleWishlist(userId = userId, productId = productId)
    }
}