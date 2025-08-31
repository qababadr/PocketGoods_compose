package com.badrqaba.core.domain.model

import java.util.Locale


data class ProductPreview(
    val id: Long,
    val title: String,
    val category: String,
    val price: Double,
    val thumbnail: String?
) {
    val priceAsString: String
        get() = String.format(Locale.CANADA, "%.2f", price)
}
