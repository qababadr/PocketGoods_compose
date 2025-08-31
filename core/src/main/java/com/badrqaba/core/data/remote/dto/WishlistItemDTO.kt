package com.badrqaba.core.data.remote.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class WishlistItemDTO(
    val id: Long,
    @SerializedName("product_id") val productId: Long?,
    @SerializedName("product_detail") val productDetail: ProductDTO?
) : Parcelable
