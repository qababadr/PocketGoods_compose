package com.badrqaba.authentication_feature.domain.repository

import com.badrqaba.core.domain.model.User
import com.badrqaba.core.util.api.Resource
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {

    fun login(email: String, password: String): Flow<User>

    suspend fun register(
        fullName: String,
        email: String,
        password: String,
        passwordConfirmation: String
    ): String

    fun getAuthenticatedUser(): Flow<Resource<User?>>

    fun getCachedAuthenticatedUser(userId: Long): Flow<User?>

    suspend fun logout(userId: Long): Boolean

    suspend fun clearCachedUser()
}