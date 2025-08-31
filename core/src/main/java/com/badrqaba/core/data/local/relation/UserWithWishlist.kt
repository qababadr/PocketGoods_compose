package com.badrqaba.core.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.badrqaba.core.data.local.entity.UserEntity
import com.badrqaba.core.data.local.entity.WishlistItemEntity

data class UserWithWishlist(
    @Embedded val user: UserEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "user_id"
    )
    val wishlist: List<WishlistItemEntity>
)
