package com.badrqaba.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.badrqaba.core.data.local.entity.WishlistItemEntity
import com.badrqaba.core.util.WISHLIST_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface WishlistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWishlist(wishlist: WishlistItemEntity)

    @Query("SELECT * FROM $WISHLIST_TABLE WHERE user_id = :userId")
    fun getWishlist(userId: Long): Flow<List<WishlistItemEntity>>

    @Query("DELETE FROM $WISHLIST_TABLE WHERE product_id = :productId AND user_id = :userId")
    suspend fun deleteWishlistItem(productId: Long, userId: Long)

    @Query("DELETE FROM $WISHLIST_TABLE WHERE user_id = :userId")
    suspend fun clearWishlist(userId: Long)
}