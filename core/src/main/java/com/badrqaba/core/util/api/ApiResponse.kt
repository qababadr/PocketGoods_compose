package com.badrqaba.core.util.api

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val data: T
)
