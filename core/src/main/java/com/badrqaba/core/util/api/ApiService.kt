package com.badrqaba.core.util.api

import com.badrqaba.core.util.API_BASE_URL
import com.google.gson.Strictness
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.delete
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.gson.gson
import jakarta.inject.Inject
import kotlinx.serialization.SerializationException

class ApiService @Inject constructor(engine: HttpClientEngine)  {

    val client = HttpClient(engine) {
        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
                serializeNulls()
                setStrictness(Strictness.LENIENT)
            }
        }

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    println("KtorLog: $message")
                }
            }
            level = LogLevel.ALL
        }

        defaultRequest {
            url(API_BASE_URL)
            header("Accept", "application/json")
        }
    }

    suspend inline fun <reified T> get(url: String, headers: Map<String, String>? = null): T {
        try {
            val response = client.get(url) {
                headers?.forEach { (key, value) ->
                    header(key, value)
                }
            }
            if (response.status.isSuccess()) {
                return response.body<ApiResponse<T>>().data
            } else {
                throw ApiError("Request failed with status: ${response.status}")
            }
        } catch (e: ClientRequestException) {
            throw e.toApiError()
        } catch (e: ServerResponseException) {
            throw e.toApiError()
        } catch (e: SerializationException) {
            throw ApiError("Failed to parse response")
        } catch (e: Exception) {
            throw ApiError("Unexpected error: ${e.message}")
        }
    }

    suspend inline fun <reified T> rawGet(url: String, headers: Map<String, String>? = null): T {
        try {
            val response = client.get(url) {
                headers?.forEach { (key, value) ->
                    header(key, value)
                }
            }
            if (response.status.isSuccess()) {
                return response.body<T>()
            } else {
                throw ApiError("Request failed with status: ${response.status}")
            }
        } catch (e: ClientRequestException) {
            throw e.toApiError()
        } catch (e: ServerResponseException) {
            throw e.toApiError()
        } catch (e: SerializationException) {
            throw ApiError("Failed to parse response")
        } catch (e: Exception) {
            throw ApiError("Unexpected error: ${e.message}")
        }
    }

    suspend inline fun <reified T, reified B> post(
        url: String,
        body: B,
        headers: Map<String, String>? = null
    ): T {
        try {
            val response = client.post(url) {
                val contentType = headers?.get("Content-Type") ?: ContentType.Application.Json.toString()
                contentType(ContentType.parse(contentType))

                headers?.forEach { (key, value) ->
                    header(key, value)
                }

                if (contentType == ContentType.Application.FormUrlEncoded.toString()) {
                    val params = Parameters.build {
                        (body as Map<*, *>).forEach { (key, value) ->
                            append(key.toString(), value.toString())
                        }
                    }

                    setBody(FormDataContent(params))
                } else {
                    setBody(body)
                }
            }

            if (response.status.isSuccess()) {
                return response.body<ApiResponse<T>>().data
            } else {
                throw ApiError("Request failed with status: ${response.status}")
            }
        } catch (e: ClientRequestException) {
            throw e.toApiError()
        } catch (e: ServerResponseException) {
            throw e.toApiError()
        } catch (e: SerializationException) {
            throw ApiError("Failed to parse response")
        } catch (e: Exception) {
            throw ApiError("Unexpected error: ${e.message}")
        }
    }


    suspend inline fun <reified T, reified B> rawPost(url: String, body: B, headers: Map<String, String>? = null): T {
        try {
            val response = client.post(url) {
                val contentType = headers?.get("Content-Type") ?: ContentType.Application.Json.toString()
                contentType(ContentType.parse(contentType))

                headers?.forEach { (key, value) ->
                    header(key, value)
                }

                if (contentType == ContentType.Application.FormUrlEncoded.toString()) {
                    val params = Parameters.build {
                        (body as Map<*, *>).forEach { (key, value) ->
                            append(key.toString(), value.toString())
                        }
                    }

                    setBody(FormDataContent(params))
                } else {
                    setBody(body)
                }
            }
            if (response.status.isSuccess()) {
                return response.body<T>()
            } else {
                throw ApiError("Request failed with status: ${response.status}")
            }
        } catch (e: ClientRequestException) {
            throw e.toApiError()
        } catch (e: ServerResponseException) {
            throw e.toApiError()
        } catch (e: SerializationException) {
            throw ApiError("Failed to parse response")
        } catch (e: Exception) {
            throw ApiError("Unexpected error: ${e.message}")
        }
    }

    suspend inline fun <reified T, reified B> put(
        url: String,
        body: B,
        headers: Map<String, String>? = null
    ): T {
        try {
            val response = client.put(url) {
                val contentType = headers?.get("Content-Type") ?: ContentType.Application.Json.toString()
                contentType(ContentType.parse(contentType))

                headers?.forEach { (key, value) ->
                    header(key, value)
                }

                if (contentType == ContentType.Application.FormUrlEncoded.toString()) {
                    val params = Parameters.build {
                        (body as Map<*, *>).forEach { (key, value) ->
                            append(key.toString(), value.toString())
                        }
                    }

                    setBody(FormDataContent(params))
                } else {
                    setBody(body)
                }
            }

            if (response.status.isSuccess()) {
                return response.body<ApiResponse<T>>().data
            } else {
                throw ApiError("Request failed with status: ${response.status}")
            }
        } catch (e: ClientRequestException) {
            throw e.toApiError()
        } catch (e: ServerResponseException) {
            throw e.toApiError()
        } catch (e: SerializationException) {
            throw ApiError("Failed to parse response")
        } catch (e: Exception) {
            throw ApiError("Unexpected error: ${e.message}")
        }
    }
}