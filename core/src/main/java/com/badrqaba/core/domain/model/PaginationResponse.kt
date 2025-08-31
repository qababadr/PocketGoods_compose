package com.badrqaba.core.domain.model

data class PaginationResponse<T>(
    val data: List<T>,
    val firstLink: String,
    val lastLink: String,
    val currentPage: Int,
    val from: Int,
    val lastPage: Int,
    val path: String,
    val perPage: Int,
    val to: Int,
    val total: Int,
    val nextLink: String? = null,
    val prevLink: String? = null
) {
    inline fun <reified  T> empty(): PaginationResponse<T> {
        return PaginationResponse(
            data = emptyList(),
            firstLink = "",
            lastLink = "",
            currentPage = 1,
            from = 1,
            lastPage = 1,
            path = "",
            perPage = 1,
            to = 1,
            nextLink = null,
            prevLink = null,
            total = 1
        )
    }
}
