package com.badrqaba.core.util.api.mock

import com.badrqaba.core.util.api.ApiResponse
import com.google.gson.Gson
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.HttpResponseData
import io.ktor.http.Headers
import io.ktor.http.HttpStatusCode

fun MockRequestHandleScope.login(data: MockEngineRequestData, responseHeaders: Headers): HttpResponseData {
    return if (data.method == "POST") {
        val email = data.formData["email"] ?: ""
        val password = data.formData["password"] ?: ""

        if (email == MockData.MOCK_EMAIL && password == MockData.MOCK_PASSWORD) {
            respond(
                Gson().toJson(MockData.loginResponse),
                HttpStatusCode.OK,
                responseHeaders
            )
        } else {
            respond(
                Gson().toJson(ApiResponse(data = null)),
                HttpStatusCode.Unauthorized,
                responseHeaders
            )
        }
    } else {
        error("Unsupported Method ${data.method} for url ${data.url}")
    }
}

fun MockRequestHandleScope.register(
    data: MockEngineRequestData,
    responseHeaders: Headers
): HttpResponseData {
    return if (data.method == "PUT") {
        val email = data.formData["email"] ?: ""
        val password = data.formData["password"] ?: ""
        val fullName = data.formData["name"] ?: ""
        val passwordConfirmation = data.formData["password_confirmation"] ?: ""
        respond(
            Gson().toJson(
                MockData.registerResponse(
                    fullName = fullName,
                    email = email,
                    password = password,
                    passwordConfirmation = passwordConfirmation
                )
            ),
            HttpStatusCode.OK,
            responseHeaders
        )
    } else {
        error("Unsupported Method ${data.method} for url ${data.url}")
    }
}

fun MockRequestHandleScope.isAuthenticated(
    data: MockEngineRequestData,
    responseHeaders: Headers
): HttpResponseData {

    val actualToken = data.headers["Authorization"]
    return if (actualToken == "Bearer ${MockData.TOKEN}") {
        respond(
            Gson().toJson(MockData.authenticatedUserResponse(hasCorrectToken = true)),
            HttpStatusCode.OK,
            responseHeaders
        )
    } else {
        respond(
            Gson().toJson(MockData.authenticatedUserResponse(hasCorrectToken = false)),
            HttpStatusCode.Unauthorized,
            responseHeaders
        )
    }
}

fun MockRequestHandleScope.logout(
    data: MockEngineRequestData,
    responseHeaders: Headers
): HttpResponseData {
    val actualToken = data.headers["Authorization"]
    return if (actualToken == "Bearer ${MockData.TOKEN}") {
        respond(
            Gson().toJson(MockData.logoutResponse(hasCorrectToken = true)),
            HttpStatusCode.OK,
            responseHeaders
        )
    } else {
        respond(
            Gson().toJson(MockData.logoutResponse(hasCorrectToken = false)),
            HttpStatusCode.Unauthorized,
            responseHeaders
        )
    }
}