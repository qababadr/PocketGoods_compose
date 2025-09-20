package com.badrqaba.pocketgoods.helper

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider

inline fun <reified A : Activity> launchApp(
    onBefore: () -> Unit = {},
    intentFactory: (Context) -> Intent = {
        Intent(
            ApplicationProvider.getApplicationContext(),
            A::class.java
        ).apply {
            putExtra("is_test_mode", true)
        }
    }
): ActivityScenario<A> {
    onBefore()
    val context = ApplicationProvider.getApplicationContext<Context>()
    return ActivityScenario.launch(intentFactory(context))
}