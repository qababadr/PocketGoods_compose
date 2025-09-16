package com.badrqaba.authentication_feature.presentation.auth

import com.badrqaba.core.domain.model.User

data class AuthState(
    val authenticatedUser: User? = null
)
