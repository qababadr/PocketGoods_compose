package com.badrqaba.product_feature.domain.repository

import com.badrqaba.core.domain.model.PaginationResponse
import com.badrqaba.core.domain.model.Product
import com.badrqaba.core.domain.model.ProductPreview
import com.badrqaba.core.util.api.Resource
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProducts(page: Int): Flow<Resource<PaginationResponse<ProductPreview>>>

    fun getProduct(id: Long): Flow<Resource<Product?>>

    fun getSuggestedProducts(query: String): Flow<Resource<List<ProductPreview>>>

    fun searchProducts(query: String, page: Int): Flow<Resource<PaginationResponse<ProductPreview>>>
}