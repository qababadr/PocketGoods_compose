package com.badrqaba.pocketgoods.robot

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.performClick
import com.badrqaba.core.data.mapper.toProductPreview
import com.badrqaba.core.domain.model.ProductPreview
import com.badrqaba.core.util.api.mock.MockData
import com.badrqaba.core_ui.R
import com.badrqaba.core_ui.util.LOADING_INDICATOR
import com.badrqaba.core_ui.util.PARENT_SCROLLABLE_CONTAINER
import com.badrqaba.core_ui.util.PRODUCT_CAROUSEL_TAG
import com.badrqaba.core_ui.util.SEARCH_INPUT_LOADING_INDICATOR

class ProductRobot(composeTestRule: ComposeTestRule) : Robot(composeTestRule) {

    private val productsPage1 = MockData.productsPaginationResponse
        .data
        .map { it.toProductPreview() }

    fun assertPageIsReady() {
        waitUntil(
            timeoutMillis = 10_000,
            condition = {
                withNode(matcher = hasTestTag(LOADING_INDICATOR))
                    .isNotDisplayed()
            }
        )
    }

    fun assertHasListOfProducts() {
        assertPageIsReady()

        productsPage1.forEach { productPreview ->
            scrollAndAssertHasCorrectData(product = productPreview)
        }
    }

    private fun scrollAndAssertHasCorrectData(product: ProductPreview) {
        scrollTo(
            scrollableContainerTag = PARENT_SCROLLABLE_CONTAINER,
            useUnmergedTree = true,
            matcher = hasContentDescription(
                value = stringResource(
                    R.string.cd_product_card,
                    product.title
                )
            )
        )

        with(product) {
            waitUntil(
                timeoutMillis = 5_000,
                condition = {
                    withNode(
                        useUnmergedTree = true,
                        matcher = hasContentDescription(
                            value = stringResource(
                                R.string.cd_image,
                                "$id $title"
                            )
                        )
                    ).isDisplayed()
                }
            )

            assertHasText(text = title)

            assertHasText(
                text = stringResource(
                    R.string.lbl_price_value,
                    priceAsString
                )
            )

            assertHasContentDescription(
                stringResource(
                    R.string.cd_button_learn_more,
                    title
                ),
                useUnmergedTree = true
            )
        }
    }

    fun assertCanNavigateToProductDetailFromProductsFragment() {
        val product = MockData
            .sunGlassesProductResponse
            .data

        with(product) {
            scrollTo(
                scrollableContainerTag = PARENT_SCROLLABLE_CONTAINER,
                useUnmergedTree = true,
                matcher = hasContentDescription(
                    stringResource(R.string.cd_product_card, title)
                )
            )

            val learnMoreButton = withNode(
                useUnmergedTree = true,
                matcher = hasContentDescription(
                    stringResource(
                        R.string.cd_button_learn_more,
                        title
                    )
                )
            )

            learnMoreButton.assertIsDisplayed()
            learnMoreButton
                .assertHasClickAction()
                .performClick()

            assertPageIsReady()

            assertHasText(text = title)
        }
    }

    fun assertHasCorrectDetail() {
        val product = MockData
            .sunGlassesProductResponse
            .data

        with(product) {
            scrollTo(
                scrollableContainerTag = PARENT_SCROLLABLE_CONTAINER,
                useUnmergedTree = true,
                matcher = hasContentDescription(
                    "$PRODUCT_CAROUSEL_TAG ${product.title}"
                )
            )

            product.media.forEach { image ->
                scrollAndAssertIsDisplayed(
                    useUnmergedTree = true,
                    matcher = hasContentDescription(
                        value = "image ${image.original}",
                    ),
                    scrollableContainerTag = PRODUCT_CAROUSEL_TAG
                )
            }

            scrollAndAssertIsDisplayed(
                scrollableContainerTag = PARENT_SCROLLABLE_CONTAINER,
                matcher = hasText(title)
            )

            scrollAndAssertIsDisplayed(
                scrollableContainerTag = PARENT_SCROLLABLE_CONTAINER,
                matcher = hasText(
                    substring = true,
                    text = stringResource(R.string.lbl_price)
                )
            )

            scrollAndAssertIsDisplayed(
                scrollableContainerTag = PARENT_SCROLLABLE_CONTAINER,
                matcher = hasText(
                    substring = true,
                    text = stringResource(
                        R.string.lbl_price_value,
                        priceAsString
                    )
                )
            )

            scrollAndAssertIsDisplayed(
                scrollableContainerTag = PARENT_SCROLLABLE_CONTAINER,
                matcher = hasText(description)
            )

            scrollAndAssertIsDisplayed(
                scrollableContainerTag = PARENT_SCROLLABLE_CONTAINER,
                matcher = hasText(
                    substring = true,
                    text = quantity.toString()
                )
            )

            scrollAndAssertIsDisplayed(
                scrollableContainerTag = PARENT_SCROLLABLE_CONTAINER,
                matcher = hasText(category)
            )
        }
    }

    fun assertCanShowProductSuggestions() {
        val searchQuery = "s"
        val correctSuggestions = MockData.suggestedProducts(query = searchQuery)

        insertText(
            matcher = hasContentDescription(
                stringResource(R.string.cd_search_products)
            ),
            text = searchQuery
        )

        waitUntil(
            timeoutMillis = 5_000,
            condition = {
                withNode(
                    matcher = hasTestTag(SEARCH_INPUT_LOADING_INDICATOR)
                ).isNotDisplayed()
            }
        )

        waitUntil(
            timeoutMillis = 5_000,
            condition = {
                withNode(
                    matcher = hasTestTag(
                        stringResource(R.string.cd_search_input_list_test_tag)
                    )
                ).isDisplayed()
            }
        )

        correctSuggestions
            .data
            .map { it.toProductPreview() }
            .forEach { suggestion ->
                scrollAndAssertIsDisplayed(
                    scrollableContainerTag = stringResource(R.string.cd_search_input_list_test_tag),
                    matcher = hasContentDescription(
                        stringResource(
                            R.string.cd_suggested_product,
                            suggestion.title
                        )
                    ),
                    useUnmergedTree = true
                )
            }
    }

    fun assertCanNavigateToProductDetailFromSuggestedProductsList() {
        val product = MockData.sunGlassesProductResponse.data

        val suggestedProductMatcher = hasContentDescription(
            stringResource(
                R.string.cd_suggested_product,
                product.title
            )
        )

        waitUntil(
            timeoutMillis = 5_000,
            condition = {
                withNode(
                    matcher = hasTestTag(
                        stringResource(R.string.cd_search_input_list_test_tag)
                    )
                ).isDisplayed()
            }
        )

        scrollAndAssertIsDisplayed(
            scrollableContainerTag = stringResource(R.string.cd_search_input_list_test_tag),
            matcher = suggestedProductMatcher,
            useUnmergedTree = true
        )

        withNode(matcher = suggestedProductMatcher)
            .assertHasClickAction()
            .performClick()

        assertPageIsReady()

        scrollTo(
            scrollableContainerTag = PARENT_SCROLLABLE_CONTAINER,
            matcher = hasContentDescription("$PRODUCT_CAROUSEL_TAG ${product.title}"),
            useUnmergedTree = true
        )

        scrollAndAssertIsDisplayed(
            scrollableContainerTag = PARENT_SCROLLABLE_CONTAINER,
            matcher = hasText(product.title)
        )
    }

    fun assertCanNavigateToSearchResultScreen(searchQuery: String) {
        insertText(
            matcher = hasContentDescription(stringResource(R.string.cd_search_products)),
            text = searchQuery
        )

        waitUntil(timeoutMillis = 5_000) {
            withNode(
                matcher = hasTestTag(SEARCH_INPUT_LOADING_INDICATOR)
            ).isNotDisplayed()
        }

        withNode(
            matcher = hasContentDescription(
                stringResource(R.string.cd_search_input_search_button)
            )
        ).assertHasClickAction().performClick()

        assertPageIsReady()
    }

    fun assertHasMatchingProducts(searchQuery: String) {
        val correctSuggestedProducts = MockData
            .searchPaginationResponse(query = searchQuery)

        correctSuggestedProducts
            .data
            .map { it.toProductPreview() }
            .forEach { product ->
                scrollAndAssertHasCorrectData(product = product)
            }
    }

    fun assertCanShowNoResultMessage() {
        assertCanNavigateToSearchResultScreen(
            searchQuery = "some random product title that does not exist"
        )

        assertIsDisplayed(
            matcher = hasText(stringResource(R.string.txt_empty_product_list))
        )
    }
}