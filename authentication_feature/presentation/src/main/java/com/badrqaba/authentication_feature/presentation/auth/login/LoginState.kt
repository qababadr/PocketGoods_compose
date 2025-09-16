package com.badrqaba.authentication_feature.presentation.auth.login

data class LoginState(
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val alertText: String = "",
    val alertVisible: Boolean = false,
    val email: String = "",
    val hasEmailError: Boolean = false,
    val password: String = "",
    val hasPasswordError: Boolean = false
)
