package com.badrqaba.core.data.remote.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class ProductPreviewDTO(
    val id: Long,
    val title: String,
    val category: String,
    val price: Double,
    val thumbnail: String?
) : Parcelable
