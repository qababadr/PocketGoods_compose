package com.badrqaba.core.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.badrqaba.core.util.WISHLIST_TABLE

@Entity(tableName = WISHLIST_TABLE)
data class WishlistItemEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    @ColumnInfo(name = "product_id")
    val productId: Long?,
    @ColumnInfo(name = "user_id")
    val userId: Long
)
