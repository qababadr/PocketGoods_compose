package com.badrqaba.core.data.remote.dto.pagination

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class PaginationResponseDTO<T: Parcelable>(
    val data: List<T>,
    val links: PaginationLinksDTO,
    val meta: PaginationMetaDTO
): Parcelable
