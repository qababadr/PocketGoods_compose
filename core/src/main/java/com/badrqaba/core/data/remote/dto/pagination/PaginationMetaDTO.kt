package com.badrqaba.core.data.remote.dto.pagination

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class PaginationMetaDTO(
    @SerializedName("current_page") val currentPage: Int,
    val from: Int,
    @SerializedName("last_page") val lastPage: Int,
    val path: String,
    @SerializedName("per_page") val perPage: Int,
    val to: Int,
    val total: Int
): Parcelable
