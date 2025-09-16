package com.badrqaba.authentication_feature.domain.use_case

import com.badrqaba.authentication_feature.domain.repository.AuthenticationRepository


class RegisterUseCase(private val repository: AuthenticationRepository) {

    suspend operator fun invoke(
        fullName: String,
        email: String,
        password: String,
        passwordConfirmation: String
    ): String {
        return repository.register(
            fullName = fullName,
            email = email,
            password = password,
            passwordConfirmation = passwordConfirmation
        )
    }
}