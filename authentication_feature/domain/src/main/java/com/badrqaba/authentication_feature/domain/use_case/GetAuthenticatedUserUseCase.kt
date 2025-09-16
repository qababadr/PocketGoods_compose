package com.badrqaba.authentication_feature.domain.use_case

import com.badrqaba.authentication_feature.domain.repository.AuthenticationRepository
import com.badrqaba.core.domain.model.User
import com.badrqaba.core.util.api.Resource
import kotlinx.coroutines.flow.Flow

class GetAuthenticatedUserUseCase(private val repository: AuthenticationRepository) {

    operator fun invoke(): Flow<Resource<User?>> {
        return repository.getAuthenticatedUser()
    }
}