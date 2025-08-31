package com.badrqaba.core.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.badrqaba.core.data.local.entity.ProductEntity
import com.badrqaba.core.data.local.entity.WishlistItemEntity

data class WishlistItemWithProductAndImages(
    @Embedded val wishlistItem: WishlistItemEntity,
    @Relation(
        entity = ProductEntity::class,
        parentColumn = "product_id",
        entityColumn = "id"
    )
    val productWithImages: ProductWithImages?,
)
