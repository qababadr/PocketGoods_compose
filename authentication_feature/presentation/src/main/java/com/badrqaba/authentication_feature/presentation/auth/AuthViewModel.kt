package com.badrqaba.authentication_feature.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.badrqaba.authentication_feature.domain.use_case.AuthenticationUseCases
import com.badrqaba.authentication_feature.presentation.auth.login.LoginFormEvent
import com.badrqaba.authentication_feature.presentation.auth.login.LoginState
import com.badrqaba.authentication_feature.presentation.auth.register.RegisterAlertType
import com.badrqaba.authentication_feature.presentation.auth.register.RegisterFormEvent
import com.badrqaba.authentication_feature.presentation.auth.register.RegisterFormState
import com.badrqaba.core.domain.model.User
import com.badrqaba.core.util.Validator
import com.badrqaba.core.util.api.Resource
import com.badrqaba.core.util.dispatcher.DefaultDispatchers
import com.badrqaba.core.util.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val useCases: AuthenticationUseCases,
    private val dispatchers: DispatcherProvider = DefaultDispatchers()
) : ViewModel() {

    private val _loginSuccessEvent = MutableSharedFlow<User>(replay = 0)
    val loginSuccessEvent = _loginSuccessEvent.asSharedFlow()

    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _loginState: MutableStateFlow<LoginState> =
        MutableStateFlow(value = LoginState())
    val loginState: StateFlow<LoginState>
        get() = _loginState.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = LoginState()
        )

    private val _registerState: MutableStateFlow<RegisterFormState> =
        MutableStateFlow(value = RegisterFormState())
    val registerState: StateFlow<RegisterFormState>
        get() = _registerState.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = RegisterFormState()
        )

    private var currentJob: Job? = null

    fun onAuthEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.OnRefreshUserData -> refreshUserData(
                onRefreshFailed = event.onRefreshFailed
            )

            is AuthEvent.SetAuthenticatedUser -> _authState.update {
                it.copy(authenticatedUser = event.user)
            }

            is AuthEvent.OnLogout -> logout(
                userId = event.userId,
                onLoggedOut = event.onLoggedOut,
                onError = event.onError
            )
        }
    }

    fun onLoginEvent(event: LoginFormEvent) {
        when (event) {
            is LoginFormEvent.Login -> login(onError = event.onError)
            is LoginFormEvent.TogglePasswordVisible -> togglePasswordVisible(formType = FormType.LoginForm)
            is LoginFormEvent.SetAlert -> setAlert(
                message = event.message,
                visible = event.visible,
                formType = FormType.LoginForm
            )

            is LoginFormEvent.OnEmailChange -> onEmailChange(
                email = event.email,
                formType = FormType.LoginForm
            )

            is LoginFormEvent.OnPasswordChange -> onPasswordChange(
                password = event.password,
                formType = FormType.LoginForm
            )
        }
    }

    fun onRegisterEvent(event: RegisterFormEvent) {
        when (event) {
            is RegisterFormEvent.OnConfirmPasswordChange -> onConfirmPasswordChange(password = event.password)

            is RegisterFormEvent.OnEmailChange -> onEmailChange(
                email = event.email,
                formType = FormType.RegisterForm
            )

            is RegisterFormEvent.OnFirstAndLastNameChange -> onFirstAndLastNameChange(
                firstAndLastName = event.firstAndLastName,
            )

            is RegisterFormEvent.OnPasswordChange -> onPasswordChange(
                password = event.password,
                formType = FormType.RegisterForm
            )

            is RegisterFormEvent.OnReset -> onReset()

            is RegisterFormEvent.Register -> onRegister(
                onRegistered = event.onRegistered,
                onError = event.onError
            )

            is RegisterFormEvent.SetAlert -> setAlert(
                message = event.message,
                visible = event.visible,
                alertType = event.alertType,
                formType = FormType.RegisterForm
            )

            is RegisterFormEvent.ToggleConfirmPasswordVisible -> onToggleConfirmPasswordVisible()
            is RegisterFormEvent.TogglePasswordVisible -> togglePasswordVisible(formType = FormType.RegisterForm)
        }
    }

    private fun onToggleConfirmPasswordVisible() {
        _registerState.update {
            it.copy(
                isConfirmPasswordVisible = !_registerState.value.isConfirmPasswordVisible
            )
        }
    }

    private fun onRegister(onRegistered: (String) -> Unit, onError: () -> Unit) {
        if (isValidRegisterForm()) {
            currentJob?.cancel()
            currentJob = viewModelScope.launch(dispatchers.io) {
                _registerState.update { it.copy(isLoading = true) }

                try {
                    useCases.register(
                        fullName = _registerState.value.name,
                        email = _registerState.value.email,
                        password = _registerState.value.password,
                        passwordConfirmation = _registerState.value.confirmPassword
                    )

                    onRegistered(_registerState.value.name)
                    onReset()

                } catch (_: Exception) {
                    onError()
                } finally {
                    _registerState.update { it.copy(isLoading = false) }
                }

            }
        }
    }

    private fun onFirstAndLastNameChange(firstAndLastName: String) {
        _registerState.update {
            it.copy(
                name = firstAndLastName,
                hasEmailError = firstAndLastName.length >= 5
            )
        }
    }

    private fun onReset() {
        _registerState.update {
            it.copy(
                name = "",
                hasNameError = false,
                email = "",
                hasEmailError = false,
                password = "",
                hasPasswordError = false,
                confirmPassword = "",
                hasConfirmPasswordError = false
            )
        }
    }

    private fun onConfirmPasswordChange(password: String) {
        _registerState.update {
            it.copy(
                confirmPassword = password,
                hasConfirmPasswordError = _registerState.value.password == password
            )
        }
    }

    private fun onPasswordChange(
        password: String,
        formType: FormType
    ) {
        when (formType) {
            FormType.LoginForm -> {
                _loginState.update {
                    it.copy(
                        password = password,
                        hasPasswordError = password.isEmpty()
                    )
                }
            }

            FormType.RegisterForm -> {
                _registerState.update {
                    it.copy(
                        password = password,
                        hasPasswordError = !Validator.isValidPassword(password = password)
                    )
                }
            }
        }
    }

    private fun onEmailChange(
        email: String,
        formType: FormType
    ) {
        when (formType) {
            FormType.LoginForm -> _loginState.update {
                it.copy(
                    email = email,
                    hasEmailError = !Validator.isValidEmail(email = email)
                )
            }

            FormType.RegisterForm -> _registerState.update {
                it.copy(
                    email = email,
                    hasEmailError = !Validator.isValidEmail(email = email)
                )
            }
        }
    }

    private fun setAlert(
        message: String,
        visible: Boolean,
        formType: FormType,
        alertType: RegisterAlertType = RegisterAlertType.ERROR
    ) {
        when (formType) {
            FormType.LoginForm -> _loginState.update {
                it.copy(
                    alertVisible = visible,
                    alertText = message,
                )
            }

            FormType.RegisterForm -> _registerState.update {
                it.copy(
                    alertVisible = visible,
                    alertText = message,
                    alertType = alertType
                )
            }
        }
    }

    private fun togglePasswordVisible(formType: FormType) {
        when (formType) {
            FormType.LoginForm -> _loginState.update {
                it.copy(
                    isPasswordVisible = !_loginState.value.isPasswordVisible
                )
            }

            FormType.RegisterForm -> _registerState.update {
                it.copy(
                    isPasswordVisible = !_registerState.value.isPasswordVisible
                )
            }
        }
    }

    private fun login(onError: (Throwable) -> Unit) {
        if (isValidLoginForm()) {
            _loginState.update {
                it.copy(isLoading = true)
            }

            currentJob?.cancel()

            try {
                currentJob = useCases
                    .login(
                        email = _loginState.value.email,
                        password = _loginState.value.password
                    )
                    .onEach { user ->
                        _authState.update {
                            it.copy(
                                authenticatedUser = user
                            )
                        }
                        _loginSuccessEvent.emit(user)
                    }
                    .catch { throwable ->
                        onError(throwable)
                        _loginState.update { it.copy(isLoading = false) }
                    }
                    .flowOn(dispatchers.io)
                    .launchIn(viewModelScope)
            } catch (exp: Exception) {
                onError(exp)
            }
        }
    }

    private fun refreshUserData(onRefreshFailed: () -> Unit) {
        currentJob?.cancel()
        currentJob = useCases
            .getAuthenticatedUser()
            .onEach { resource ->
                when (resource) {
                    is Resource.Success -> _authState.update {
                        it.copy(authenticatedUser = resource.data)
                    }

                    is Resource.Error -> {
                        onRefreshFailed()
                    }

                    else -> Unit
                }
            }
            .flowOn(dispatchers.io)
            .launchIn(viewModelScope)
    }

    private fun logout(
        userId: Long,
        onLoggedOut: () -> Unit,
        onError: () -> Unit
    ) {
        currentJob?.cancel()
        currentJob = viewModelScope.launch(dispatchers.io) {
            try {
                useCases.logout(userId = userId)

                _authState.update { it.copy(authenticatedUser = null) }

                onLoggedOut()
            } catch (_: Exception) {
                onError()
            }
        }
    }

    private fun isValidRegisterForm(): Boolean {
        val emailValid = Validator.isValidEmail(email = _registerState.value.email)
        val passwordValid = Validator.isValidPassword(password = _registerState.value.password)
        val confirmNotEmpty = _registerState.value.confirmPassword.isNotEmpty()
        val passwordsMatch = _registerState.value.password == _registerState.value.confirmPassword
        val nameLongEnough = _registerState.value.name.length >= 5

        return emailValid
                && passwordValid
                && confirmNotEmpty
                && passwordsMatch
                && nameLongEnough
    }

    private fun isValidLoginForm(): Boolean {
        return Validator.isValidEmail(email = _loginState.value.email)
                && _loginState.value.password.isNotEmpty()
    }
}