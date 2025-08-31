package com.badrqaba.core.util.api.mock

import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.toByteArray
import io.ktor.client.request.HttpRequestData
import io.ktor.http.Parameters
import io.ktor.http.content.OutgoingContent
import java.net.URLDecoder

suspend fun MockRequestHandleScope.prepareRequestData(request: HttpRequestData): MockEngineRequestData {
    val url = request.url.encodedPath
    val method = request.method.value

    val query = request.url.parameters.entries().joinToString("&") { "${it.key}=${it.value.first()}" }

    val path = if (query.isNotEmpty()) {
        "$url?$query"
    } else {
        url
    }

    val bodyString = (request.body as? OutgoingContent.ByteArrayContent)
        ?.toByteArray()
        ?.decodeToString()
        .orEmpty()

    val formData = Parameters.build {
        bodyString.split("&").forEach { param ->
            val parts = param.split("=")
            val key = URLDecoder.decode(parts[0], "UTF-8")
            val value = if (parts.size > 1) {
                URLDecoder.decode(parts[1], "UTF-8")
            } else {
                ""
            }
            append(key, value)
        }
    }

    return MockEngineRequestData(
        url = url,
        method = method,
        query = query,
        path = path,
        formData = formData,
        headers = request.headers
    )
}