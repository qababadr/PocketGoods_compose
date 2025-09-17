package com.badrqaba.wishlist_feature.domain.repository

import com.badrqaba.core.domain.model.WishlistItem
import com.badrqaba.core.util.api.Resource
import kotlinx.coroutines.flow.Flow

interface WishlistRepository {
    suspend fun toggleWishlist(userId: Long, productId: Long): Long

    fun getEntireWishlist(userId: Long): Flow<Resource<List<WishlistItem>>>
}