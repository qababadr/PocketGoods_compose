package com.badrqaba.authentication_feature.presentation.auth.login

sealed class LoginFormEvent {
    data class Login(
        val onError: (Throwable) -> Unit
    ): LoginFormEvent()

    data class OnEmailChange(val email: String): LoginFormEvent()

    data class OnPasswordChange(val password: String): LoginFormEvent()

    data class SetAlert(
        val message: String,
        val visible: Boolean
    ): LoginFormEvent()

    data object TogglePasswordVisible: LoginFormEvent()
}