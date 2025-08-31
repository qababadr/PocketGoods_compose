package com.badrqaba.core.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.badrqaba.core.data.local.entity.UserEntity
import com.badrqaba.core.data.local.entity.WishlistItemEntity

data class UserWithWishlistAndProductAndImages(
    @Embedded val user: UserEntity,

    @Relation(
        entity = WishlistItemEntity::class,
        parentColumn = "id",
        entityColumn = "user_id"
    )
    val wishlist: List<WishlistItemWithProductAndImages>
)
