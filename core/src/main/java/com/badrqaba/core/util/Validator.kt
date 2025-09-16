package com.badrqaba.core.util

import android.util.Patterns

open class Validator {

    companion object {
        private const val DEFAULT_PASSWORD_PATTERN = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$"

        fun isValidPassword(
            password: String,
            pattern: String = DEFAULT_PASSWORD_PATTERN
        ): Boolean {
            return pattern.toRegex().matches(input = password)
        }

        fun isValidEmail(email: String): Boolean {
            return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }
}