package com.badrqaba.product_feature.domain.use_case

import com.badrqaba.core.domain.model.PaginationResponse
import com.badrqaba.core.domain.model.ProductPreview
import com.badrqaba.core.util.api.Resource
import com.badrqaba.product_feature.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

class GetProductsUseCase(private val repository: ProductRepository) {
    operator fun invoke(page: Int): Flow<Resource<PaginationResponse<ProductPreview>>> {
        return repository.getProducts(page = page)
    }
}