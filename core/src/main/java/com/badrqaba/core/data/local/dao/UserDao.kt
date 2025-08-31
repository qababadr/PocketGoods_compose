package com.badrqaba.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.badrqaba.core.data.local.entity.UserEntity
import com.badrqaba.core.data.local.relation.UserWithWishlistAndProductAndImages
import com.badrqaba.core.util.USERS_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Transaction
    @Query("SELECT * FROM $USERS_TABLE WHERE id = :userId")
    fun getCachedUserWithWishlist(userId: Long): Flow<UserWithWishlistAndProductAndImages>

    @Query("DELETE FROM $USERS_TABLE WHERE id = :userId")
    suspend fun clear(userId: Long)
}