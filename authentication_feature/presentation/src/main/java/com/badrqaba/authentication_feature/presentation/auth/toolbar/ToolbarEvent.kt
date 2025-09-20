package com.badrqaba.authentication_feature.presentation.auth.toolbar

sealed class ToolbarEvent {
    data object ToggleFormType : ToolbarEvent()

    data object CloseModal : ToolbarEvent()

    data object OpenModal : ToolbarEvent()
}