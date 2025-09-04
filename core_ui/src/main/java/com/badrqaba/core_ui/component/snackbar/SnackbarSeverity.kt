package com.badrqaba.core_ui.component.snackbar

sealed class SnackbarSeverity {
    data object Success : SnackbarSeverity()
    data object Error : SnackbarSeverity()
    data object Info : SnackbarSeverity()
    data object Warning : SnackbarSeverity()
}