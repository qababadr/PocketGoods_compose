package com.badrqaba.authentication_feature.presentation.auth.register

enum class RegisterAlertType {
    SUCCESS,
    ERROR
}

sealed class RegisterFormEvent {
    data object TogglePasswordVisible: RegisterFormEvent()
    data object ToggleConfirmPasswordVisible: RegisterFormEvent()

    data class SetAlert(
        val message: String,
        val visible: Boolean,
        val alertType: RegisterAlertType
    ): RegisterFormEvent()

    data class Register(
        val onRegistered: (String) -> Unit,
        val onError: () -> Unit
    ): RegisterFormEvent()

    data class OnFirstAndLastNameChange(val firstAndLastName: String): RegisterFormEvent()
    data class OnEmailChange(val email: String): RegisterFormEvent()
    data class OnPasswordChange(val password: String): RegisterFormEvent()
    data class OnConfirmPasswordChange(val password: String): RegisterFormEvent()

    data object OnReset: RegisterFormEvent()
}