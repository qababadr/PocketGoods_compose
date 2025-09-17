package com.badrqaba.wishlist_feature.domain.use_case

import com.badrqaba.core.domain.model.WishlistItem
import com.badrqaba.core.util.api.Resource
import com.badrqaba.wishlist_feature.domain.repository.WishlistRepository
import kotlinx.coroutines.flow.Flow

class GetEntireWishlistUseCase(private val repository: WishlistRepository) {

    operator fun invoke(userId: Long): Flow<Resource<List<WishlistItem>>> {
        return repository.getEntireWishlist(userId = userId)
    }
}