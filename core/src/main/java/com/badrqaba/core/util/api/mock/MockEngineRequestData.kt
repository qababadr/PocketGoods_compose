package com.badrqaba.core.util.api.mock

import io.ktor.http.Headers
import io.ktor.http.Parameters

data class MockEngineRequestData(
    val url: String,
    val method: String,
    val query: String,
    val path: String,
    val formData: Parameters,
    val headers: Headers
)
