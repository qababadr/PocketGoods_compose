package com.badrqaba.product_feature.domain.use_case

data class ProductUseCases(
    val getProducts: GetProductsUseCase,
    val getProduct: GetProductUseCase,
    val getSuggestedProducts: GetSuggestedProductsUseCase,
    val searchProducts: SearchProductsUseCase
)
