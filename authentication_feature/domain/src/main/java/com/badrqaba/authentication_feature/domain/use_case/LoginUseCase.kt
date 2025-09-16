package com.badrqaba.authentication_feature.domain.use_case

import com.badrqaba.authentication_feature.domain.repository.AuthenticationRepository
import com.badrqaba.core.domain.model.User
import kotlinx.coroutines.flow.Flow

class LoginUseCase(private val repository: AuthenticationRepository) {

    operator fun invoke(
        email: String,
        password: String
    ): Flow<User> {
        return repository.login(email = email, password= password)
    }
}