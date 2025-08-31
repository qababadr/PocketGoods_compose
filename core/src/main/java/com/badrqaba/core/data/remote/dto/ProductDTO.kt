package com.badrqaba.core.data.remote.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class ProductDTO(
    val id: Long,
    val title: String,
    val category: String,
    val price: Double,
    val quantity: Int,
    val description: String,
    val media: List<ImageDTO>
): Parcelable
