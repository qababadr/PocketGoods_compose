package com.badrqaba.authentication_feature.presentation.auth.register

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
import com.badrqaba.core_ui.R
import com.badrqaba.core_ui.component.ProgressButton
import com.badrqaba.core_ui.component.TextField
import com.badrqaba.core_ui.theme.PocketGoodsTheme
import com.badrqaba.core_ui.util.errorColor
import com.badrqaba.core_ui.util.successColor

@Composable
fun RegisterForm(
    state: RegisterFormState,
    onEvent: (RegisterFormEvent) -> Unit,
    onLoginClick: () -> Unit,
    onCloseClick: () -> Unit,
) {
    val context = LocalContext.current

    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {

        AnimatedVisibility(visible = state.alertVisible) {
            Surface(
                tonalElevation = 8.dp,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth(),
                color = if (state.alertType == RegisterAlertType.SUCCESS)
                    MaterialTheme.successColor()
                else MaterialTheme.errorColor(),
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        painter = painterResource(
                            id = if (state.alertType == RegisterAlertType.SUCCESS)
                                R.drawable.check_circle
                            else R.drawable.information
                        ),
                        contentDescription = state.alertText,
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
            value = state.name,
            onValueChange = {
                onEvent(
                    RegisterFormEvent.OnFirstAndLastNameChange(
                        firstAndLastName = it
                    )
                )
            },
            label = stringResource(id = R.string.lbl_first_and_last_name),
            leadingIcon = R.drawable.tag,
            isError = state.hasNameError,
            textError =
                stringResource(id = R.string.err_fullName_validation_error),
        )

        TextField(
            value = state.email,
            onValueChange = { onEvent(RegisterFormEvent.OnEmailChange(email = it)) },
            label = stringResource(id = R.string.lbl_email),
            leadingIcon = R.drawable.email_outline,
            isError = state.hasEmailError,
            textError =
                stringResource(id = R.string.err_email_validation_error),
            keyboardOptions =
                KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        TextField(
            value = state.password,
            onValueChange = { onEvent(RegisterFormEvent.OnPasswordChange(password = it)) },
            label = stringResource(id = R.string.lbl_password),
            trailingIcon = if (state.isPasswordVisible) R.drawable.eye
            else R.drawable.eye_closed,
            visualTransformation = if (state.isPasswordVisible)
                VisualTransformation.None
            else PasswordVisualTransformation(),
            isError = state.hasPasswordError,
            textError =
                stringResource(id = R.string.err_password_validation_error),
            keyboardOptions =
                KeyboardOptions(keyboardType = KeyboardType.Password),
            onTrailingIconClick =
                { onEvent(RegisterFormEvent.TogglePasswordVisible) },
            leadingIcon = R.drawable.lock
        )

        TextField(
            value = state.confirmPassword,
            onValueChange = {
                onEvent(
                    RegisterFormEvent.OnConfirmPasswordChange(
                        password = it
                    )
                )
            },
            label = stringResource(id = R.string.lbl_confirm_password),
            trailingIcon = if (state.isConfirmPasswordVisible)
                R.drawable.eye else R.drawable.eye_closed,
            visualTransformation = if (state.isConfirmPasswordVisible)
                VisualTransformation.None
            else PasswordVisualTransformation(),
            isError = state.hasConfirmPasswordError,
            textError =
                stringResource(id = R.string.err_confirm_password_validation_error),
            keyboardOptions =
                KeyboardOptions(keyboardType = KeyboardType.Password),
            onTrailingIconClick =
                { onEvent(RegisterFormEvent.ToggleConfirmPasswordVisible) },
            leadingIcon = R.drawable.lock
        )

        Text(
            text = stringResource(id = R.string.lbl_already_registered)
                .uppercase(),
            style = MaterialTheme.typography.bodyMedium,
            textDecoration = TextDecoration.Underline,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .clickable { onLoginClick() }
                .padding(top = 12.dp)
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
                        contentDescription =
                            context.getString(R.string.cd_button_register)
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
                        RegisterFormEvent.Register(
                            onRegistered = { registeredUsername ->
                                onEvent(
                                    RegisterFormEvent.SetAlert(
                                        message = context.getString(
                                            R.string.txt_registered,
                                            registeredUsername
                                        ),
                                        visible = true,
                                        alertType = RegisterAlertType.SUCCESS
                                    )
                                )
                            },
                            onError = {
                                onEvent(
                                    RegisterFormEvent.SetAlert(
                                        message =
                                            context.getString(R.string.err_registration),
                                        visible = true,
                                        alertType = RegisterAlertType.ERROR
                                    )
                                )
                            }
                        )
                    )
                }
            ) { contentColor ->
                Text(
                    text = stringResource(id = R.string.lbl_register),
                    color = contentColor,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(horizontal = 6.dp)
                )
            }
        }
    }
}

@Composable
private fun RegisterPreviewWrapper(state: RegisterFormState) {
    PocketGoodsTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            RegisterForm(
                state = state,
                onEvent = {},
                onLoginClick = {},
                onCloseClick = {}
            )
        }
    }
}

@Preview(name = "Default State")
@Composable
private fun RegisterFormPreview_Default() {
    RegisterPreviewWrapper(state = RegisterFormState())
}

@Preview(name = "Name Error")
@Composable
private fun RegisterFormPreview_NameError() {
    RegisterPreviewWrapper(
        state = RegisterFormState(
            name = "Bad",
            hasNameError = true
        )
    )
}

@Preview(name = "Email Error")
@Composable
private fun RegisterFormPreview_EmailError() {
    RegisterPreviewWrapper(
        state = RegisterFormState(
            name = "User Name",
            email = "invalid-email",
            hasEmailError = true
        )
    )
}

@Preview(name = "Password Error")
@Composable
private fun RegisterFormPreview_PasswordError() {
    RegisterPreviewWrapper(
        state = RegisterFormState(
            name = "User Name",
            password = "",
            hasPasswordError = true
        )
    )
}

@Preview(name = "Confirm Password Error")
@Composable
private fun RegisterFormPreview_ConfirmPasswordError() {
    RegisterPreviewWrapper(
        state = RegisterFormState(
            name = "User Name",
            password = "pass1234",
            confirmPassword = "pass5678",
            hasConfirmPasswordError = true
        )
    )
}

@Preview(name = "Alert Visible")
@Composable
private fun RegisterFormPreview_AlertVisible() {
    RegisterPreviewWrapper(
        state = RegisterFormState(
            alertVisible = true,
            alertText = "Registration failed",
            alertType = RegisterAlertType.ERROR
        )
    )
}

@Preview(name = "Success Alert")
@Composable
private fun RegisterFormPreview_SuccessAlert() {
    RegisterPreviewWrapper(
        state = RegisterFormState(
            alertVisible = true,
            alertText = "Successfully registered!",
            alertType = RegisterAlertType.SUCCESS
        )
    )
}

@Preview(name = "Loading State")
@Composable
private fun RegisterFormPreview_Loading() {
    RegisterPreviewWrapper(
        state = RegisterFormState(
            name = "User Name",
            email = "user@example.com",
            password = "password123",
            confirmPassword = "password123",
            isLoading = true
        )
    )
}
