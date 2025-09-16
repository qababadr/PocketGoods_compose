package com.badrqaba.authentication_feature.presentation.auth

import com.badrqaba.core.domain.model.User

sealed class AuthEvent {
    data class OnRefreshUserData(
        val onRefreshFailed: () -> Unit
    ): AuthEvent()

    data class SetAuthenticatedUser(
        val user: User?
    ): AuthEvent()
}