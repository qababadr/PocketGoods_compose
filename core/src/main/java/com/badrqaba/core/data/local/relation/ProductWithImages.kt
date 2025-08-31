package com.badrqaba.core.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.badrqaba.core.data.local.entity.ImageEntity
import com.badrqaba.core.data.local.entity.ProductEntity

data class ProductWithImages(
    @Embedded val product: ProductEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "model_id"
    )
    val images: List<ImageEntity>
)
