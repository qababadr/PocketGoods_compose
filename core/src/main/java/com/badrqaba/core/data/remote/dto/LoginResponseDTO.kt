package com.badrqaba.core.data.remote.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class LoginResponseDTO(
    val user: UserDTO,
    val token: String,
): Parcelable
