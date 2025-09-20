package com.badrqaba.pocketgoods.end2end

import androidx.test.filters.LargeTest
import com.badrqaba.authentication_feature.data.di.AuthenticationFeatureDomainModule
import com.badrqaba.core.data.di.CoreDataModule
import com.badrqaba.pocketgoods.helper.EndToEndTest
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
class ProductSearchScreenEndToEndTest: EndToEndTest() {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    override fun onSetup() {
        hiltRule.inject()
    }

    @Test
    fun typing_in_search_bar_should_show_product_suggestions() {
        launchActivity()

        with(ProductRobot(composeRule)) {
            assertPageIsReady()
            assertCanShowProductSuggestions()
        }
    }

    @Test
    fun clicking_on_suggested_product_should_navigate_to_product_detail() {
        launchActivity()

        with(ProductRobot(composeRule)) {
            assertPageIsReady()
            assertCanShowProductSuggestions()
            assertCanNavigateToProductDetailFromSuggestedProductsList()
        }
    }

    @Test
    fun submitting_search_query_should_display_matching_products() {
        val searchQuery = "s"

        launchActivity()

        with(ProductRobot(composeRule)) {
            assertPageIsReady()
            assertCanNavigateToSearchResultScreen(searchQuery = searchQuery)
            assertHasMatchingProducts(searchQuery = searchQuery)
        }
    }

    @Test
    fun submitting_some_non_existing_product_title_as_search_query_should_show_no_result_message() {
        launchActivity()

        with(ProductRobot(composeRule)) {
            assertPageIsReady()
            assertCanShowNoResultMessage()
        }
    }
}