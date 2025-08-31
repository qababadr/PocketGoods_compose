package com.badrqaba.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.badrqaba.core.util.PRODUCTS_TABLE

@Entity(tableName = PRODUCTS_TABLE)
data class ProductEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val title: String,
    val category: String,
    val price: Double,
    val quantity: Int,
    val description: String,
)
