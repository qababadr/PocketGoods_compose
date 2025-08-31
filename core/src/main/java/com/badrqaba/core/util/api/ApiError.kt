package com.badrqaba.core.util.api

import io.ktor.client.plugins.*
import io.ktor.client.statement.*

class ApiError(
    override val message: String,
    val causeException: Throwable? = null
) : Exception(message, causeException)

suspend fun ResponseException.toApiError(): ApiError {
    val body = response.bodyAsText()
    return ApiError(
        message = "HTTP ${response.status.value}: $body",
        causeException = this
    )
}
