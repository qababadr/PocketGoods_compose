package com.badrqaba.authentication_feature.domain.use_case

data class AuthenticationUseCases(
    val login: LoginUseCase,
    val register: RegisterUseCase,
    val getAuthenticatedUser: GetAuthenticatedUserUseCase,
    val logout: LogoutUseCase,
    val clearCachedUser: ClearCachedUserUseCase,
    val getCachedAuthenticatedUser: GetCachedAuthenticatedUser
)
