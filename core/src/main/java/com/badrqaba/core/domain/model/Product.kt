package com.badrqaba.core.domain.model

import java.util.Locale

data class Product(
    val id: Long,
    val title: String,
    val category: String,
    val price: Double,
    val quantity: Int,
    val description: String,
    val media: List<Image>
) {
    val inStock: Boolean
        get() = quantity > 0

    val priceAsString: String
        get() = String.format(Locale.CANADA, "%.2f", price)
}
