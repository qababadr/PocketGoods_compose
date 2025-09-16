package com.badrqaba.core.data.mapper

import android.os.Parcelable
import com.badrqaba.core.data.remote.dto.SearchPaginationResponseDTO
import com.badrqaba.core.data.remote.dto.pagination.PaginationResponseDTO
import com.badrqaba.core.domain.model.PaginationResponse
import com.badrqaba.core.domain.model.SearchPaginationResponse

inline fun <reified DTO : Parcelable, Model> PaginationResponseDTO<DTO>.toPaginationResponse(
    mapper: (DTO) -> Model
): PaginationResponse<Model> {
    return PaginationResponse(
        data = data.map(mapper),
        firstLink = links.first,
        lastLink = links.last,
        currentPage = meta.currentPage,
        from = meta.from,
        lastPage = meta.lastPage,
        path = meta.path,
        perPage = meta.perPage,
        to = meta.to,
        total = meta.total,
        nextLink = links.next,
        prevLink = links.prev
    )
}

inline fun <reified DTO : Parcelable, Model>
        SearchPaginationResponseDTO<DTO>.toSearchPagination(
    mapper: (DTO) -> Model
): SearchPaginationResponse<Model> {
    return SearchPaginationResponse(
        data = data.map(mapper),
        lastPage = lastPage
    )
}