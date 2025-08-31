package com.badrqaba.core.data.remote.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class UserDTO(
    val id: Long,
    val name: String,
    val email: String,
    @SerializedName("email_verified_at") val emailVerifiedAt: String?,
    val wishlist: List<WishlistItemDTO>
) : Parcelable
