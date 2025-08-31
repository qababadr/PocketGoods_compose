package com.badrqaba.core.data.remote.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class ImageDTO(
    val uuid: String,
    val filename: String,
    val preview: String,
    val original: String,
): Parcelable