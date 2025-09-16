package com.badrqaba.authentication_feature.presentation.auth.register

data class RegisterFormState(
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val alertText: String = "",
    val alertVisible: Boolean = false,
    val alertType: RegisterAlertType = RegisterAlertType.ERROR,
    val name: String = "",
    val hasNameError: Boolean = false,
    val email: String = "",
    val hasEmailError: Boolean = false,
    val password: String = "",
    val hasPasswordError: Boolean = false,
    val confirmPassword: String = "",
    val hasConfirmPasswordError: Boolean = false
)
