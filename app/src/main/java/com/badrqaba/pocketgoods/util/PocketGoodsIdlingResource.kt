package com.badrqaba.pocketgoods.util

import androidx.test.espresso.IdlingResource
import java.util.concurrent.atomic.AtomicBoolean

class PocketGoodsIdlingResource : IdlingResource {

    companion object {
        const val LOG_TAG = "PocketGoodsIdlingResource"
    }

    @Volatile
    private var resourceCallback: IdlingResource.ResourceCallback? = null

    private val isIdle = AtomicBoolean(true)

    override fun getName(): String? {
        return this.javaClass.name
    }

    override fun isIdleNow(): Boolean {
        return isIdle.get()
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.resourceCallback = callback
    }

    fun setIdleState(isIdleNow: Boolean) {
        if (isIdleNow == isIdle.get()) return

        isIdle.set(isIdleNow)

        if (isIdleNow) {
            resourceCallback?.onTransitionToIdle()
        }
    }
}