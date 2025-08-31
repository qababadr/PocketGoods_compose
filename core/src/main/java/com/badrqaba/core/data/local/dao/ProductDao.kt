package com.badrqaba.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.badrqaba.core.data.local.entity.ProductEntity
import com.badrqaba.core.data.local.relation.ProductWithImages
import com.badrqaba.core.util.PRODUCTS_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)

    @Transaction
    @Query("SELECT * FROM $PRODUCTS_TABLE WHERE id = :productId")
    fun getProduct(productId: Long): Flow<ProductWithImages>

    @Query("DELETE FROM $PRODUCTS_TABLE")
    suspend fun deleteAllProducts()
}