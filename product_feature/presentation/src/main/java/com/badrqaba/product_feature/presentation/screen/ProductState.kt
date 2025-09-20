package com.badrqaba.product_feature.presentation.screen

import com.badrqaba.core.domain.model.Product
import com.badrqaba.core.domain.model.ProductPreview

data class ProductState(
    val products: List<ProductPreview> = emptyList(),
    val searchResults: List<ProductPreview> = emptyList(),
    val lastPage: Int = 1,
    val isPageLoading: Boolean = true,
    val currentPage: Int = 1,
    val error: Throwable? = null,
    val searchQuery: String  = "",
    val isSearching: Boolean = false,
    val product: Product? = null,
    val suggestedProducts: List<ProductPreview> = emptyList()
)
