package com.badrqaba.authentication_feature.domain.use_case

import com.badrqaba.authentication_feature.domain.repository.AuthenticationRepository

class LogoutUseCase(private val repository: AuthenticationRepository) {

    suspend operator fun invoke(userId: Long): Boolean {
        return repository.logout(userId = userId)
    }
}