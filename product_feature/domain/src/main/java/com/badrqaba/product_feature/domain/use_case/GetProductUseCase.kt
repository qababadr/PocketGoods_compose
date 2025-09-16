package com.badrqaba.product_feature.domain.use_case

import com.badrqaba.core.domain.model.Product
import com.badrqaba.core.util.api.Resource
import com.badrqaba.product_feature.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow


class GetProductUseCase(private val repository: ProductRepository) {

    operator fun invoke(id: Long): Flow<Resource<Product?>> {
        return repository.getProduct(id= id)
    }
}