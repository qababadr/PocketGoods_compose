package com.badrqaba.pocketgoods.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import com.badrqaba.authentication_feature.presentation.auth.AuthEvent
import com.badrqaba.authentication_feature.presentation.auth.AuthViewModel
import com.badrqaba.authentication_feature.presentation.auth.toolbar.ToolbarViewModel
import com.badrqaba.core_ui.component.snackbar.SnackbarController
import com.badrqaba.core_ui.theme.PocketGoodsTheme
import com.badrqaba.pocketgoods.util.LocalIsTestMode
import com.badrqaba.pocketgoods.util.PocketGoodsIdlingResource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel by viewModels<MainViewModel>()
    private val authViewModel by viewModels<AuthViewModel>()

    private val snackController: SnackbarController = SnackbarController(lifecycleScope)

    private var idlingResource: PocketGoodsIdlingResource? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        actionBar?.hide()

        mainViewModel.onEvent(
            event = MainActivityEvent.OnLoadData(
                onSuccess = { user ->
                    authViewModel.onAuthEvent(
                        event = AuthEvent.SetAuthenticatedUser(user = user)
                    )
                },
                onError = {
                    authViewModel.onAuthEvent(
                        event = AuthEvent.SetAuthenticatedUser(user = null)
                    )
                },
                onStartLoading = ::onStartLoading,
                onDoneLoading = ::onDoneLoading
            )
        )

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                mainViewModel.state.value.isSplashScreenVisible
            }
        }

        val isTestMode = intent
            ?.getBooleanExtra("is_test_mode", false)
            ?: false

        setContent {
            CompositionLocalProvider(LocalIsTestMode provides isTestMode) {
                val mainActivityState by mainViewModel.state.collectAsState()

                PocketGoodsTheme(isDarkTheme = mainActivityState.isDarkTheme) {

                    val toolbarViewModel = hiltViewModel<ToolbarViewModel>()

                    val authState by authViewModel.authState.collectAsState()
                    val mainActivityState by mainViewModel.state.collectAsState()
                    val loginState by authViewModel.loginState.collectAsState()
                    val registerState by authViewModel.registerState.collectAsState()
                    val toolbarState by toolbarViewModel.state.collectAsState()

                    Layout(
                        loginSuccessEvents = authViewModel.loginSuccessEvent,
                        authState = authState,
                        onAuthEvent = authViewModel::onAuthEvent,
                        mainActivityState = mainActivityState,
                        onMainEvent = mainViewModel::onEvent,
                        toolbarState = toolbarState,
                        onToolbarEvent = toolbarViewModel::onEvent,
                        loginState = loginState,
                        onLoginEvent = authViewModel::onLoginEvent,
                        registerState = registerState,
                        onRegisterEvent = authViewModel::onRegisterEvent,
                        snackbarController = snackController,
                    )
                }
            }
        }
    }

    @VisibleForTesting
    fun getIdlingResource(): PocketGoodsIdlingResource? {
        if (idlingResource == null) {
            idlingResource = PocketGoodsIdlingResource()
        }
        return idlingResource
    }

    @VisibleForTesting
    private fun onStartLoading() {
        idlingResource?.setIdleState(false)
        Log.d(PocketGoodsIdlingResource.LOG_TAG, "Not idle")
    }

    @VisibleForTesting
    private fun onDoneLoading() {
        idlingResource?.setIdleState(true)
        Log.d(PocketGoodsIdlingResource.LOG_TAG, "Is now idle")
    }
}