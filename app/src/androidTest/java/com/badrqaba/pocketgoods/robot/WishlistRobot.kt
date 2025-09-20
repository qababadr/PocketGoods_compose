package com.badrqaba.pocketgoods.robot

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.performClick
import com.badrqaba.core.data.mapper.toProductPreview
import com.badrqaba.core.util.api.mock.MockData
import com.badrqaba.core_ui.R
import com.badrqaba.core_ui.util.LOADING_INDICATOR
import com.badrqaba.core_ui.util.PARENT_SCROLLABLE_CONTAINER

class WishlistRobot(composeTestRule: ComposeTestRule) : Robot(composeTestRule) {
    fun assertUseCanAddProductToWishlist() {
        val productToAdd = MockData
            .productsPaginationResponse
            .data
            .first { it.id == 2L }
            .toProductPreview()

        with(productToAdd) {
            val clickableImage = withNode(
                useUnmergedTree = true,
                matcher = hasContentDescription(
                    stringResource(
                        R.string.cd_product_card_image_button,
                        title
                    )
                )
            )

            scrollTo(
                useUnmergedTree = true,
                scrollableContainerTag = PARENT_SCROLLABLE_CONTAINER,
                matcher = hasContentDescription(
                    stringResource(
                        R.string.cd_product_card,
                        title
                    )
                )
            )

            waitUntil(
                timeoutMillis = 5_000,
                condition = {
                    clickableImage.isDisplayed()
                }
            )

            clickableImage
                .assertHasClickAction()
                .performClick()

            waitUntil(
                timeoutMillis = 5_000,
                condition = {
                    withNode(
                        matcher = hasContentDescription(
                            stringResource(
                                R.string.cd_product_not_in_wishlist,
                                title
                            )
                        )
                    ).isDisplayed()
                }
            )

            withNode(
                matcher = hasContentDescription(
                    stringResource(
                        R.string.cd_toggle_wishlist,
                        title
                    )
                ),
                useUnmergedTree = true
            ).assertHasClickAction().performClick()

            waitUntil(
                timeoutMillis = 5_000,
                condition = {
                    withNode(
                        matcher = hasContentDescription(
                            stringResource(
                                R.string.cd_product_in_wishlist,
                                title
                            )
                        )
                    ).isDisplayed()
                }
            )
        }
    }

    fun assertUseCanRemoveProductFromWishlist() {
        val productToRemove =
            MockData
                .productsPaginationResponse
                .data
                .first { it.id == 3L }
                .toProductPreview()

        with(productToRemove) {
            val clickableImage = withNode(
                useUnmergedTree = true,
                matcher = hasContentDescription(
                    value = stringResource(
                        R.string.cd_product_card_image_button,
                        title
                    )
                )
            )

            scrollTo(
                useUnmergedTree = true,
                scrollableContainerTag = PARENT_SCROLLABLE_CONTAINER,
                matcher = hasContentDescription(
                    stringResource(
                        R.string.cd_product_card,
                        title
                    )
                )
            )

            waitUntil(
                timeoutMillis = 5_000,
                condition = {
                    clickableImage.isDisplayed()
                }
            )

            clickableImage
                .assertHasClickAction()
                .performClick()

            waitUntil(timeoutMillis = 5_000) {
                withNode(
                    matcher = hasContentDescription(
                        stringResource(
                            R.string.cd_product_in_wishlist,
                            title
                        )
                    ),
                ).isDisplayed()
            }

            withNode(
                matcher = hasContentDescription(
                    stringResource(
                        R.string.cd_toggle_wishlist,
                        title
                    )
                ),
                useUnmergedTree = true
            ).assertHasClickAction().performClick()

            waitUntil(timeoutMillis = 5_000) {
                withNode(
                    matcher = hasContentDescription(
                        stringResource(
                            R.string.cd_product_not_in_wishlist,
                            title
                        )
                    ),
                ).isDisplayed()
            }
        }
    }

    fun assertCanDeleteProductFromAuthenticatedUserWishlist() {
        val productToDelete = MockData
            .productsPaginationResponse
            .data
            .first { it.id == 3L }
            .toProductPreview()

        waitUntil(
            timeoutMillis = 10_000,
            condition = {
                withNode(matcher = hasTestTag(LOADING_INDICATOR))
                    .isNotDisplayed()
            }
        )

        with(productToDelete) {
            val openDeleteModalButton = withNode(
                matcher = hasContentDescription(
                    stringResource(
                        R.string.cd_open_delete_modal,
                        title
                    )
                ),
                useUnmergedTree = true
            )

            val confirmDeleteButton = withNode(
                matcher = hasContentDescription(
                    stringResource(
                        R.string.cd_delete_wishlist_item,
                        id.toString()
                    )
                ),
                useUnmergedTree = true
            )

            openDeleteModalButton
                .assertHasClickAction()
                .performClick()

            waitUntil(timeoutMillis = 5_000) {
                confirmDeleteButton.isDisplayed()
            }

            confirmDeleteButton
                .assertHasClickAction()
                .performClick()

            waitUntil(
                timeoutMillis = 5_000,
                condition = {
                    withNode(
                        matcher = hasTestTag(LOADING_INDICATOR)
                    ).isNotDisplayed()
                }
            )

            waitUntil(
                timeoutMillis = 5_000,
                condition = {
                    withNode(
                        matcher = hasText(title)
                    ).isNotDisplayed()
                }
            )
        }
    }
}