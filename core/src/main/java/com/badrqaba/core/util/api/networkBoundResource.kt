package com.badrqaba.core.util.api

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.first

inline fun <Result, Request> networkBoundResource(
    crossinline databaseQuery: () -> Flow<Result>,
    crossinline apiCall: suspend () -> Request,
    crossinline saveApiCallResult: suspend (Request) -> Unit,
    crossinline shouldFetch: (Result) -> Boolean = { true },
    crossinline onApiCallSuccess: () -> Unit = { },
    crossinline onApiCallFailed: (Throwable) -> Unit = { },
) = channelFlow {
    val data = databaseQuery().first()

    if (shouldFetch(data)) {

        send(Resource.Loading(data))

        try {
            val apiResponse = apiCall()
            saveApiCallResult(apiResponse)
            onApiCallSuccess()

            databaseQuery().collect {
                send(Resource.Success(it))
            }
        } catch (throwable: Throwable) {
            onApiCallFailed(throwable)
            databaseQuery().collect {
                send(Resource.Error(it, throwable))
            }
        }
    } else {
        databaseQuery().collect {
            send(Resource.Success(it))
        }
    }
}
