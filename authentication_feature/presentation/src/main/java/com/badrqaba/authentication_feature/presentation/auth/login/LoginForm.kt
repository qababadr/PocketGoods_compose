package com.badrqaba.authentication_feature.presentation.auth.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.badrqaba.core.domain.model.User
import com.badrqaba.core_ui.R
import com.badrqaba.core_ui.component.ProgressButton
import com.badrqaba.core_ui.component.TextField
import com.badrqaba.core_ui.theme.PocketGoodsTheme
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun LoginForm(
    state: LoginState,
    onEvent: (LoginFormEvent) -> Unit,
    onRegisterNowClick: () -> Unit,
    onCloseClick: () -> Unit,
    onLoggedIn: (User) -> Unit,
    loginSuccessEvents: SharedFlow<User>? = null
) {

    val context = LocalContext.current

    LaunchedEffect(loginSuccessEvents) {
        loginSuccessEvents?.collect { user ->
            onLoggedIn(user)
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {

        AnimatedVisibility(visible = state.alertVisible) {
            Surface(
                tonalElevation = 8.dp,
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        painter = painterResource(id = R.drawable.information),
                        contentDescription = stringResource(id = R.string.txt_wrong_login_credentials),
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .size(size = 40.dp)
                            .padding(vertical = 4.dp),
                    )

                    Text(
                        text = state.alertText,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(all = 8.dp)
                    )
                }
            }
        }

        TextField(
            value = state.email,
            onValueChange = { onEvent(LoginFormEvent.OnEmailChange(email = it)) },
            label = stringResource(id = R.string.lbl_email),
            leadingIcon = R.drawable.email_outline,
            isError = state.hasEmailError,
            textError = stringResource(id = R.string.err_email_validation_error),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        )

        TextField(
            value = state.password,
            onValueChange = { onEvent(LoginFormEvent.OnPasswordChange(password = it)) },
            label = stringResource(id = R.string.lbl_password),
            trailingIcon = if (state.isPasswordVisible) R.drawable.eye else R.drawable.eye_closed,
            visualTransformation = if (state.isPasswordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            isError = state.hasPasswordError,
            textError = stringResource(id = R.string.err_password_required),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            onTrailingIconClick = { onEvent(LoginFormEvent.TogglePasswordVisible) },
            leadingIcon = R.drawable.lock,
        )

        Text(
            text = stringResource(id = R.string.lbl_register_now).uppercase(),
            style = MaterialTheme.typography.bodyMedium,
            textDecoration = TextDecoration.Underline,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .clickable { onRegisterNowClick() }
                .padding(top = 12.dp)
                .semantics {
                    contentDescription = context.getString(R.string.cd_register_now)
                }
        )

        HorizontalDivider()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = MaterialTheme.colorScheme.secondary,
                ),
                onClick = onCloseClick,
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = stringResource(id = R.string.lbl_close),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSecondary,
                )
            }

            Spacer(modifier = Modifier.width(width = 12.dp))

            ProgressButton(
                isLoading = state.isLoading,
                modifier = Modifier
                    .wrapContentWidth(align = Alignment.CenterHorizontally)
                    .semantics {
                        contentDescription = context.getString(R.string.cd_button_login)
                    },
                shape = MaterialTheme.shapes.small,
                buttonColors = ButtonDefaults.buttonColors().copy(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                ),
                onClick = {
                    onEvent(
                        LoginFormEvent.Login(
                            onError = { throwable ->
                                onEvent(
                                    LoginFormEvent.SetAlert(
                                        message = context.getString(
                                            R.string.txt_wrong_login_credentials
                                        ),
                                        visible = true
                                    )
                                )
                            }
                        )
                    )
                }
            ) { contentColor ->
                Text(
                    text = stringResource(id = R.string.lbl_login),
                    color = contentColor,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(horizontal = 6.dp)
                )
            }
        }
    }
}

@Composable
private fun PreviewWrapper(state: LoginState) {
    PocketGoodsTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            LoginForm(
                state = state,
                onEvent = {},
                onCloseClick = {},
                onRegisterNowClick = {},
                onLoggedIn = {}
            )
        }
    }
}

@Preview(name = "Default State")
@Composable
private fun LoginFormPreview_Default() {
    PreviewWrapper(state = LoginState())
}

@Preview(name = "Email Error")
@Composable
private fun LoginFormPreview_EmailError() {
    PreviewWrapper(
        state = LoginState(
            email = "invalid-email",
            hasEmailError = true
        )
    )
}

@Preview(name = "Password Error")
@Composable
private fun LoginFormPreview_PasswordError() {
    PreviewWrapper(
        state = LoginState(
            email = "user@example.com",
            password = "",
            hasPasswordError = true
        )
    )
}

@Preview(name = "Alert Visible")
@Composable
private fun LoginFormPreview_AlertVisible() {
    PreviewWrapper(
        state = LoginState(
            alertVisible = true,
            alertText = "Invalid credentials"
        )
    )
}

@Preview(name = "Loading State")
@Composable
private fun LoginFormPreview_Loading() {
    PreviewWrapper(
        state = LoginState(
            email = "user@example.com",
            password = "password123",
            isLoading = true
        )
    )
}