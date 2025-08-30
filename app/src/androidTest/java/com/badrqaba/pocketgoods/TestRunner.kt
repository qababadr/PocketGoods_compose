package com.badrqaba.pocketgoods

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

class TestRunner: AndroidJUnitRunner() {
    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application? {
        //TODO add Hilt test application
        return super.newApplication(cl, className, context)
    }
}