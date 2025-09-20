package com.badrqaba.pocketgoods.helper

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.IdlingPolicies
import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.badrqaba.authentication_feature.data.di.AuthenticationFeatureDomainModule
import com.badrqaba.core.data.di.CoreDataModule
import com.badrqaba.pocketgoods.main.MainActivity
import com.badrqaba.pocketgoods.util.PocketGoodsIdlingResource
import com.badrqaba.product_feature.data.di.ProductFeatureDomainModule
import com.badrqaba.settings_feature.data.di.ApplicationSettingsFeatureDomainModule
import com.badrqaba.wishlist_feature.data.di.WishlistFeatureDomainModule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@HiltAndroidTest
@UninstallModules(
    CoreDataModule::class,
    WishlistFeatureDomainModule::class,
    ProductFeatureDomainModule::class,
    AuthenticationFeatureDomainModule::class,
    ApplicationSettingsFeatureDomainModule::class,
)
@LargeTest
@RunWith(AndroidJUnit4::class)
abstract class EndToEndTest {

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    private lateinit var idlingResource: PocketGoodsIdlingResource
    private lateinit var scenario: ActivityScenario<MainActivity>

    abstract fun onSetup()

    @Before
    fun setUp() {
        onSetup()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        IdlingRegistry
            .getInstance()
            .unregister(idlingResource)

        Dispatchers.resetMain()
    }

    fun launchActivity() {
        scenario = launchApp<MainActivity>()
        scenario.onActivity { mainActivity ->
            idlingResource = mainActivity.getIdlingResource()!!

            IdlingRegistry
                .getInstance()
                .register(idlingResource)

            IdlingPolicies.setIdlingResourceTimeout(30, TimeUnit.SECONDS)
        }
    }
}