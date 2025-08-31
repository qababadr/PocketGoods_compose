package com.badrqaba.core.data.mapper

import com.badrqaba.core.data.local.entity.ProductEntity
import com.badrqaba.core.data.local.relation.ProductWithImages
import com.badrqaba.core.data.remote.dto.ProductDTO
import com.badrqaba.core.data.remote.dto.ProductPreviewDTO
import com.badrqaba.core.domain.model.Product
import com.badrqaba.core.domain.model.ProductPreview

fun ProductWithImages.toProduct(): Product {
    return Product(
        id = product.id,
        title = product.title,
        category = product.category,
        price = product.price,
        quantity = product.quantity,
        description = product.description,
        media = images.map { it.toImage() }
    )
}

fun Product.toProductEntity(): ProductEntity {
    return ProductEntity(
        id = id,
        title = title,
        category = category,
        price = price,
        quantity = quantity,
        description = description
    )
}

fun ProductDTO.toProductEntity(): ProductEntity {
    return ProductEntity(
        id = id,
        title = title,
        category = category,
        price = price,
        quantity = quantity,
        description = description
    )
}

fun ProductPreviewDTO.toProductPreview(): ProductPreview {
    return ProductPreview(
        id = id,
        title = title,
        category = category,
        price = price,
        thumbnail = thumbnail
    )
}

fun ProductDTO.toProduct(): Product {
    return Product(
        id = id,
        title = title,
        category = category,
        price = price,
        quantity = quantity,
        description = description,
        media = media.map { it.toImage() }
    )
}
