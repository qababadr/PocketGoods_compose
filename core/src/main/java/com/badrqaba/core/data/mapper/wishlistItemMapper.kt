package com.badrqaba.core.data.mapper

import com.badrqaba.core.data.local.entity.WishlistItemEntity
import com.badrqaba.core.data.local.relation.WishlistItemWithProductAndImages
import com.badrqaba.core.data.remote.dto.WishlistItemDTO
import com.badrqaba.core.domain.model.WishlistItem

fun WishlistItemEntity.toWishlistItem(): WishlistItem {
    return WishlistItem(
        id = id,
        productId = productId,
        productDetail = null
    )
}

fun WishlistItem.toWishlistItemEntity(userId: Long): WishlistItemEntity{
    return WishlistItemEntity(
        id = id,
        productId = productId,
        userId = userId
    )
}

fun WishlistItemWithProductAndImages.toWishlistItem(): WishlistItem {
    return WishlistItem(
        id = wishlistItem.id,
        productId = wishlistItem.productId,
        productDetail = productWithImages?.toProduct()
    )
}

fun WishlistItemDTO.toWishlistItem(): WishlistItem {
    return WishlistItem(
        id = id,
        productId = productId,
        productDetail = productDetail?.toProduct()
    )
}

fun WishlistItemDTO.toWishlistItemEntity(userId: Long): WishlistItemEntity {
    return WishlistItemEntity(
        id = id,
        productId = productId,
        userId = userId
    )
}