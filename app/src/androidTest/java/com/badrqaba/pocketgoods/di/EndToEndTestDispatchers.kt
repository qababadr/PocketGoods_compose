package com.badrqaba.pocketgoods.di

import com.badrqaba.core.util.dispatcher.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher


@OptIn(ExperimentalCoroutinesApi::class)
class EndToEndTestDispatchers : DispatcherProvider {

    private val testDispatcher = StandardTestDispatcher()

    override val main: CoroutineDispatcher
        get() = testDispatcher
    override val io: CoroutineDispatcher
        get() = Dispatchers.IO
    override val default: CoroutineDispatcher
        get() = testDispatcher
    override val unconfined: CoroutineDispatcher
        get() = UnconfinedTestDispatcher()

}