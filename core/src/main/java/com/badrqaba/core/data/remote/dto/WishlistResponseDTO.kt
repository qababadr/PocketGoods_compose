package com.badrqaba.core.data.remote.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class WishlistResponseDTO(
    @SerializedName("in_wishlist") val inWishlist: Boolean,
    @SerializedName("wishlist_item_id") val wishlistItemId: Long
): Parcelable
