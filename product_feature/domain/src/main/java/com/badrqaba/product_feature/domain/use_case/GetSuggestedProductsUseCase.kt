package com.badrqaba.product_feature.domain.use_case

import com.badrqaba.core.domain.model.ProductPreview
import com.badrqaba.core.util.api.Resource
import com.badrqaba.product_feature.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

class GetSuggestedProductsUseCase(private val repository: ProductRepository) {

    operator fun invoke(query: String): Flow<Resource<List<ProductPreview>>> {
        return repository.getSuggestedProducts(query = query)
    }
}