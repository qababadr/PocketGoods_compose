package com.badrqaba.authentication_feature.presentation.auth.toolbar

sealed class ToolbarEvent {
    data object ToggleFormType : ToolbarEvent()

    data object CloseModal : ToolbarEvent()

    data object OpenModal : ToolbarEvent()

    data class OnLogout(
        val userId: Long,
        val onLoggedOut: () -> Unit,
        val onError: () -> Unit
    ) : ToolbarEvent()
}