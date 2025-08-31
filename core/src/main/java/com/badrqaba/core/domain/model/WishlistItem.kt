package com.badrqaba.core.domain.model

data class WishlistItem(
    val id: Long,
    val productId: Long?,
    val productDetail: Product?
)
