package com.badrqaba.pocketgoods.end2end

import androidx.test.filters.LargeTest
import com.badrqaba.authentication_feature.data.di.AuthenticationFeatureDomainModule
import com.badrqaba.core.data.di.CoreDataModule
import com.badrqaba.pocketgoods.helper.EndToEndTest
import com.badrqaba.pocketgoods.robot.AuthenticationRobot
import com.badrqaba.pocketgoods.robot.ProductRobot
import com.badrqaba.product_feature.data.di.ProductFeatureDomainModule
import com.badrqaba.settings_feature.data.di.ApplicationSettingsFeatureDomainModule
import com.badrqaba.wishlist_feature.data.di.WishlistFeatureDomainModule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(
    CoreDataModule::class,
    WishlistFeatureDomainModule::class,
    ProductFeatureDomainModule::class,
    AuthenticationFeatureDomainModule::class,
    ApplicationSettingsFeatureDomainModule::class,
)
@LargeTest
class AuthenticationEndToEndTest: EndToEndTest() {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    override fun onSetup() {
        hiltRule.inject()
    }

    @Test
    fun test_user_should_be_able_to_login_when_providing_correct_credentials() {
        launchActivity()

        with(ProductRobot(composeRule)) {
            assertPageIsReady()
        }

        with(AuthenticationRobot(composeRule)) {
            assertUserCanLogin()
        }
    }

    @Test
    fun test_user_can_register() {
        launchActivity()

        with(ProductRobot(composeRule)) {
            assertPageIsReady()
        }

        with(AuthenticationRobot(composeRule)) {
            assertUserCanRegister()
        }
    }

    @Test
    fun test_user_can_logout() {
        launchActivity()

        with(ProductRobot(composeRule)) {
            assertPageIsReady()
        }

        with(AuthenticationRobot(composeRule)) {
            assertUserCanLogin()
            assertCanLogout()
        }
    }
}