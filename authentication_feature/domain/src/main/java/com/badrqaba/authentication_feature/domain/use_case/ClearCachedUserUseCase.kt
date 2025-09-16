package com.badrqaba.authentication_feature.domain.use_case

import com.badrqaba.authentication_feature.domain.repository.AuthenticationRepository


class ClearCachedUserUseCase(private val repository: AuthenticationRepository) {

    suspend operator fun invoke() {
        repository.clearCachedUser()
    }
}