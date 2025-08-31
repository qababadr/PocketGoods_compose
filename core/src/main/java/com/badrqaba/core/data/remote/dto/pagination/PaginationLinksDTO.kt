package com.badrqaba.core.data.remote.dto.pagination

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class PaginationLinksDTO(
    val first: String,
    val last: String,
    val next: String?,
    val prev: String?
): Parcelable
