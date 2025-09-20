package com.badrqaba.pocketgoods.end2end

import androidx.test.filters.LargeTest
import com.badrqaba.authentication_feature.data.di.AuthenticationFeatureDomainModule
import com.badrqaba.core.data.di.CoreDataModule
import com.badrqaba.core.util.api.mock.MockData
import com.badrqaba.pocketgoods.helper.EndToEndTest
import com.badrqaba.pocketgoods.robot.AuthenticationRobot
import com.badrqaba.pocketgoods.robot.ProductRobot
import com.badrqaba.pocketgoods.robot.WishlistRobot
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
class WishlistEndToEndTest : EndToEndTest() {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    override fun onSetup() {
        hiltRule.inject()
    }

    @Test
    fun test_authenticated_user_can_toggle_wishlist(){
        launchActivity()

        MockData.resetWishlist()

        with(ProductRobot(composeRule)) {
            assertPageIsReady()
        }

        with(AuthenticationRobot(composeRule)) {
            assertUserCanLogin()
        }

        with(WishlistRobot(composeRule)) {
            assertUseCanAddProductToWishlist()
            assertUseCanRemoveProductFromWishlist()
        }
    }

    @Test
    fun test_authenticated_user_can_navigate_to_wishlist_manager_screen_and_delete_product_from_wishlist() {
        launchActivity()

        MockData.resetWishlist()

        with(ProductRobot(composeRule)) {
            assertPageIsReady()
        }

        with(AuthenticationRobot(composeRule)) {
            assertUserCanLogin()
            assertCanClickOnWishlistManagerMenuItem()
        }

        with(WishlistRobot(composeRule)) {
            assertCanDeleteProductFromAuthenticatedUserWishlist()
        }
    }
}