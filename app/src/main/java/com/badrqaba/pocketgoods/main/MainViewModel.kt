package com.badrqaba.pocketgoods.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.badrqaba.authentication_feature.domain.use_case.AuthenticationUseCases
import com.badrqaba.core.domain.model.AppSettings
import com.badrqaba.core.domain.model.User
import com.badrqaba.core.util.api.Resource
import com.badrqaba.core.util.dispatcher.DefaultDispatchers
import com.badrqaba.core.util.dispatcher.DispatcherProvider
import com.badrqaba.core_ui.component.snackbar.SnackbarData
import com.badrqaba.core_ui.util.CURRENT_DESTINATION_KEY
import com.badrqaba.core_ui.util.Screen
import com.badrqaba.settings_feature.domain.use_case.ApplicationSettingsUseCases
import com.badrqaba.wishlist_feature.domain.use_case.WishlistUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authenticationUseCases: AuthenticationUseCases,
    private val settingsUseCase: ApplicationSettingsUseCases,
    private val wishlistUseCases: WishlistUseCases,
    private val dispatchers: DispatcherProvider = DefaultDispatchers(),
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(
        MainActivityState(
            latestVisitedScreen =
                savedStateHandle[CURRENT_DESTINATION_KEY] ?: Screen.HomeScreen.route
        )
    )
    val state: StateFlow<MainActivityState> = _state.asStateFlow()

    private var currentJob: Job? = null

    init {
        settingsUseCase
            .getApplicationSettings()
            .onEach { settings ->
                _state.update {
                    it.copy(
                        isDarkTheme = settings.isDarkMode
                    )
                }
            }
            .flowOn(dispatchers.io)
            .launchIn(viewModelScope)
    }

    fun onEvent(event: MainActivityEvent) {
        when (event) {
            is MainActivityEvent.OnLoadData -> loadData(
                onSuccess = event.onSuccess,
                onError = event.onError,
                onStartLoading = event.onStartLoading,
                onDoneLoading = event.onDoneLoading
            )

            is MainActivityEvent.OnSetSnackbarData -> setSnackbarData(
                data = event.data
            )

            is MainActivityEvent.OnSetCurrentDestination
                -> setCurrentDestination(destination = event.currentDestination)

            is MainActivityEvent.OnSetDarkTheme -> setDarkTheme(isDarkTheme = event.isDarkTheme)

            is MainActivityEvent.OnToggleWishlist -> toggleWishlist(
                userId = event.userId,
                productId = event.productId,
                onAdded = event.onAdded,
                onRemoved = event.onRemoved
            )
        }
    }

    private fun toggleWishlist(
        userId: Long,
        productId: Long,
        onAdded: () -> Unit,
        onRemoved: () -> Unit
    ) {
        currentJob?.cancel()
        currentJob = viewModelScope.launch(dispatchers.io) {
            val result = runCatching {
                wishlistUseCases.toggleWishlist(
                    userId = userId,
                    productId = productId
                )
            }

            result
                .onSuccess { insertedWishlistItem ->
                    if (insertedWishlistItem == -1L) {
                        onRemoved()
                    } else {
                        onAdded()
                    }
                }
        }
    }

    private fun setDarkTheme(isDarkTheme: Boolean) {
        viewModelScope.launch(dispatchers.io) {
            settingsUseCase.saveApplicationSettings(
                settings = AppSettings(isDarkTheme)
            )
        }
    }

    private fun setCurrentDestination(destination: String) {
        savedStateHandle[CURRENT_DESTINATION_KEY] = destination
        _state.update {
            it.copy(latestVisitedScreen = destination)
        }
    }

    private fun setSnackbarData(data: SnackbarData) {
        _state.update {
            it.copy(
                snackbarData = data
            )
        }
    }

    private fun loadData(
        onSuccess: (User?) -> Unit,
        onError: () -> Unit,
        onStartLoading: (() -> Unit)?,
        onDoneLoading: (() -> Unit)?
    ) {
        _state.update { it.copy(isSplashScreenVisible = true) }

        onStartLoading?.invoke()

        authenticationUseCases
            .getAuthenticatedUser()
            .onEach { resource ->
                when(resource) {
                    is Resource.Success -> {
                        onSuccess(resource.data)
                        _state.update {
                            it.copy(isSplashScreenVisible = false)
                        }
                        onDoneLoading?.invoke()
                    }

                    is Resource.Error -> {
                        authenticationUseCases.clearCachedUser()
                        onError()
                        _state.update {
                            it.copy(isSplashScreenVisible = false)
                        }
                    }

                    else -> Unit
                }
            }
            .flowOn(dispatchers.io)
            .launchIn(viewModelScope)
    }
}