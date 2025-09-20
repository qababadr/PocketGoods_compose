package com.badrqaba.authentication_feature.presentation.auth.toolbar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.badrqaba.authentication_feature.presentation.auth.FormType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ToolbarViewModel @Inject constructor() : ViewModel() {
    private val _state: MutableStateFlow<ToolbarState> =
        MutableStateFlow(value = ToolbarState())
    val state: StateFlow<ToolbarState>
        get() = _state.stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = ToolbarState()
            )

    fun onEvent(event: ToolbarEvent) {
        when (event) {
            is ToolbarEvent.ToggleFormType -> toggleFormType()
            is ToolbarEvent.CloseModal -> closeModal()
            is ToolbarEvent.OpenModal -> openModal()
        }
    }

    private fun openModal() {
        _state.value = _state.value.copy(isModalVisible = true)
    }

    private fun closeModal() {
        _state.value = _state.value.copy(isModalVisible = false)
    }

    private fun toggleFormType() {
        _state.value = when(_state.value.formType) {
            FormType.LoginForm -> _state.value.copy(formType = FormType.RegisterForm)
            FormType.RegisterForm -> _state.value.copy(formType = FormType.LoginForm)
        }
    }
}