package com.badrqaba.core.domain.model

data class SearchPaginationResponse<T>(
    val data: List<T>,
    val lastPage: Int
)
