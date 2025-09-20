package com.badrqaba.core_ui.component.snackbar

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SnackbarController(private val scope: CoroutineScope) {
    private var snackbarJob: Job? = null

    init {
        cancelActiveJob()
    }

    fun showSnackbar(
        snackbarHostState: SnackbarHostState,
        message: String,
        actionLabel: String = "",
        onDismiss: (() -> Unit)? = null,
        onActionPerformed: (() -> Unit)? = null,
        duration: SnackbarDuration = SnackbarDuration.Long
    ) {
        if (snackbarJob == null) {
            snackbarJob = scope.launch {
                snackbarHostState.showSnackbar(
                    message = message,
                    actionLabel = actionLabel,
                    duration = duration
                ).let { snackBarResults->
                    when(snackBarResults){
                        SnackbarResult.Dismissed -> {
                            onDismiss?.invoke()
                        }
                        SnackbarResult.ActionPerformed -> {
                            if(onActionPerformed != null) onActionPerformed()
                        }
                    }
                }
                cancelActiveJob()
            }
        } else {
            cancelActiveJob()
            snackbarJob = scope.launch {
                val snackBarResults = snackbarHostState.showSnackbar(
                    message = message,
                    actionLabel = actionLabel,
                    duration = duration
                )
                when(snackBarResults){
                    SnackbarResult.Dismissed -> {
                        onDismiss?.invoke()
                    }
                    SnackbarResult.ActionPerformed -> {
                        if(onActionPerformed != null) onActionPerformed()
                    }
                }
                cancelActiveJob()
            }
        }
    }

    private fun cancelActiveJob() {
        snackbarJob?.let { job ->
            job.cancel()
            snackbarJob= Job()
        }
    }
}