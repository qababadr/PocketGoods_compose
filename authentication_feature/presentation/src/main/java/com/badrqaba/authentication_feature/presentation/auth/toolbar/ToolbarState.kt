package com.badrqaba.authentication_feature.presentation.auth.toolbar

import com.badrqaba.authentication_feature.presentation.auth.FormType

data class ToolbarState(
    val formType: FormType = FormType.LoginForm,
    val isModalVisible : Boolean = false
)
