package com.badrqaba.core.data.remote.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class SearchPaginationResponseDTO<T: Parcelable>(
    val data: List<T>,
    @SerializedName("last_page") val lastPage: Int,
): Parcelable
