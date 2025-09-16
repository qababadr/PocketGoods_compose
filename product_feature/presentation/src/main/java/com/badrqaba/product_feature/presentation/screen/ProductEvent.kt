package com.badrqaba.product_feature.presentation.screen

sealed class ProductEvent {
    data object GetProducts : ProductEvent()
    data object OnNextPage : ProductEvent()
    data object LoadProduct : ProductEvent()
    data object SearchSuggestions : ProductEvent()
    data object SearchProducts : ProductEvent()
    data object ClearSearch: ProductEvent()
    data class OnQueryChange(val query: String) : ProductEvent()
}