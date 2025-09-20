package com.badrqaba.authentication_feature.presentation.auth.toolbar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.badrqaba.authentication_feature.presentation.auth.AuthState
import com.badrqaba.authentication_feature.presentation.auth.FormType
import com.badrqaba.authentication_feature.presentation.auth.login.LoginForm
import com.badrqaba.authentication_feature.presentation.auth.login.LoginFormEvent
import com.badrqaba.authentication_feature.presentation.auth.login.LoginState
import com.badrqaba.authentication_feature.presentation.auth.register.RegisterAlertType
import com.badrqaba.authentication_feature.presentation.auth.register.RegisterForm
import com.badrqaba.authentication_feature.presentation.auth.register.RegisterFormEvent
import com.badrqaba.authentication_feature.presentation.auth.register.RegisterFormState
import com.badrqaba.authentication_feature.presentation.component.UserMenu
import com.badrqaba.core.data.mapper.toUser
import com.badrqaba.core.domain.model.User
import com.badrqaba.core.util.api.mock.MockData
import com.badrqaba.core.util.stringAvatar
import com.badrqaba.core_ui.R
import com.badrqaba.core_ui.component.Modal
import com.badrqaba.core_ui.theme.PocketGoodsTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

@Composable
fun Toolbar(
    authState: AuthState,
    toolbarState: ToolbarState,
    loginState: LoginState,
    registerState: RegisterFormState,
    isDarkTheme: Boolean,
    onToolbarEvent: (ToolbarEvent) -> Unit,
    onLoginFormEvent: (LoginFormEvent) -> Unit,
    onRegisterFormEvent: (RegisterFormEvent) -> Unit,
    onLogout: () -> Unit,
    onLoggedIn: (User) -> Unit,
    onUserMenuClick: () -> Unit,
    onWishlistClick: () -> Unit,
    onDarkThemeSwitched: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    loginSuccessEvents: SharedFlow<User>? = null
) {
    val context = LocalContext.current
    Box(modifier = modifier) {

        if (authState.authenticatedUser == null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .clickable {
                        onToolbarEvent(ToolbarEvent.OpenModal)
                    }
                    .semantics(mergeDescendants = true) {
                        contentDescription = context.getString(R.string.cd_toolbar_login)
                    }
            ) {
                Text(
                    text = stringResource(id = R.string.lbl_login).uppercase(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Icon(
                    painter = painterResource(id = R.drawable.login),
                    contentDescription = stringResource(R.string.lbl_login),
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.requiredSize(size = 20.dp)
                )
            }

            Modal(
                isOpen = toolbarState.isModalVisible,
                title = {
                    Text(
                        text = stringResource(
                            id = if (toolbarState.formType == FormType.LoginForm) R.string.lbl_login
                            else R.string.lbl_register
                        ),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(start = 6.dp)
                    )
                },
                headerColor = MaterialTheme.colorScheme.primary,
                onDismiss = {
                    onToolbarEvent(ToolbarEvent.CloseModal)
                }
            ) {
                Box(modifier = Modifier.padding(all = 12.dp)) {
                    AnimatedVisibility(
                        visible = toolbarState.formType == FormType.LoginForm,
                        enter = fadeIn() + slideInHorizontally(initialOffsetX = { it / 2 }),
                        exit = fadeOut() + slideOutHorizontally(targetOffsetX = { it })
                    ) {
                        LoginForm(
                            state = loginState,
                            onEvent = onLoginFormEvent,
                            onRegisterNowClick = { onToolbarEvent(ToolbarEvent.ToggleFormType) },
                            onCloseClick = { onToolbarEvent(ToolbarEvent.CloseModal) },
                            onLoggedIn = onLoggedIn,
                            loginSuccessEvents = loginSuccessEvents
                        )
                    }

                    AnimatedVisibility(
                        visible = toolbarState.formType == FormType.RegisterForm,
                        enter = fadeIn() + slideInHorizontally(initialOffsetX = { it / 2 }),
                        exit = fadeOut() + slideOutHorizontally(targetOffsetX = { it })
                    ) {
                        RegisterForm(
                            state = registerState,
                            onEvent = onRegisterFormEvent,
                            onCloseClick = { onToolbarEvent(ToolbarEvent.CloseModal) },
                            onLoginClick = { onToolbarEvent(ToolbarEvent.ToggleFormType) },
                        )
                    }
                }
            }

        } else {
            UserMenu(
                onLogout = onLogout,
                onClick = onUserMenuClick,
                onWishlistClick = onWishlistClick,
                onDarkThemeSwitched = onDarkThemeSwitched,
                initials = authState
                    .authenticatedUser
                    .name
                    .stringAvatar(),
                wishlistCount = authState.authenticatedUser.wishlist.size,
                isDarkTheme = isDarkTheme,
            )
        }
    }
}

@PreviewLightDark
@Preview()
@Composable
private fun ToolbarPreview() {
    var authState by remember { mutableStateOf(AuthState()) }
    var toolbarState by remember { mutableStateOf(ToolbarState()) }
    var loginState by remember { mutableStateOf(LoginState()) }
    var registerState by remember { mutableStateOf(RegisterFormState()) }
    var isDarkTheme by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    PocketGoodsTheme {
        Box(
            modifier = Modifier
                .padding(top = 80.dp)
                .background(color = Color.Blue)
                .fillMaxSize()
        ) {
            Toolbar(
                modifier = Modifier.padding(20.dp),
                authState = authState,
                loginState = loginState,
                registerState = registerState,
                toolbarState = toolbarState,
                isDarkTheme = isDarkTheme,
                onToolbarEvent = { event ->
                    toolbarState = when (event) {
                        ToolbarEvent.CloseModal -> toolbarState.copy(
                            isModalVisible = false
                        )

                        ToolbarEvent.OpenModal -> toolbarState.copy(
                            isModalVisible = true
                        )

                        ToolbarEvent.ToggleFormType -> toolbarState.copy(
                            formType = if (toolbarState.formType == FormType.LoginForm)
                                FormType.RegisterForm else
                                FormType.LoginForm
                        )
                    }
                },
                onLoginFormEvent = { event ->
                    when (event) {
                        is LoginFormEvent.Login -> {
                            scope.launch {
                                loginState = loginState.copy(
                                    isLoading = true
                                )

                                delay(1500)

                                if (loginState.email == MockData.MOCK_EMAIL &&
                                    loginState.password == MockData.MOCK_PASSWORD
                                ) {
                                    authState = authState.copy(
                                        authenticatedUser = MockData
                                            .userDTO
                                            .toUser()
                                    )
                                    loginState = loginState.copy(
                                        isLoading = false
                                    )
                                } else {
                                    loginState = loginState.copy(
                                        alertVisible = true,
                                        alertText = "Wrong credentials"
                                    )
                                }

                                loginState = loginState.copy(
                                    isLoading = false
                                )
                            }
                        }

                        is LoginFormEvent.OnEmailChange -> loginState = loginState.copy(
                            email = event.email
                        )

                        is LoginFormEvent.OnPasswordChange -> loginState = loginState.copy(
                            password = event.password
                        )

                        is LoginFormEvent.SetAlert -> Unit

                        LoginFormEvent.TogglePasswordVisible -> loginState = loginState.copy(
                            isPasswordVisible = !loginState.isPasswordVisible
                        )
                    }
                },
                onRegisterFormEvent = { event ->
                    when (event) {
                        is RegisterFormEvent.OnConfirmPasswordChange -> registerState =
                            registerState.copy(
                                confirmPassword = event.password
                            )

                        is RegisterFormEvent.OnEmailChange -> registerState =
                            registerState.copy(
                                email = event.email
                            )

                        is RegisterFormEvent.OnFirstAndLastNameChange -> registerState =
                            registerState.copy(
                                name = event.firstAndLastName
                            )

                        is RegisterFormEvent.OnPasswordChange -> registerState =
                            registerState.copy(
                                password = event.password
                            )

                        RegisterFormEvent.OnReset -> Unit
                        is RegisterFormEvent.Register -> {
                            scope.launch {

                                registerState = registerState.copy(
                                    isLoading = true
                                )

                                delay(1500)

                                if (registerState.email.isNotEmpty()) {
                                    registerState = registerState.copy(
                                        alertText = if (registerState.email == MockData.MOCK_EMAIL)
                                            "Email exists" else "Welcome to our store",
                                        alertVisible = true,
                                        alertType = if (registerState.email == MockData.MOCK_EMAIL)
                                            RegisterAlertType.ERROR else RegisterAlertType.SUCCESS
                                    )
                                }
                                registerState = registerState.copy(
                                    isLoading = false
                                )
                            }
                        }

                        is RegisterFormEvent.SetAlert -> Unit
                        RegisterFormEvent.ToggleConfirmPasswordVisible -> registerState =
                            registerState.copy(
                                isPasswordVisible = !registerState.isConfirmPasswordVisible
                            )

                        RegisterFormEvent.TogglePasswordVisible -> registerState =
                            registerState.copy(
                                isPasswordVisible = !registerState.isPasswordVisible
                            )
                    }
                },
                onLoggedIn = { _ ->
                    toolbarState = toolbarState.copy(
                        isModalVisible = false
                    )
                },
                onLogout = {
                    scope.launch {
                        delay(1500)
                        authState = authState.copy(
                            authenticatedUser = null
                        )
                    }
                },
                onUserMenuClick = {},
                onWishlistClick = {},
                onDarkThemeSwitched = {
                    isDarkTheme = it
                }
            )
        }
    }
}