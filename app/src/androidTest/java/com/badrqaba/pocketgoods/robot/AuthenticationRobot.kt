package com.badrqaba.pocketgoods.robot

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.performClick
import com.badrqaba.core.util.api.mock.MockData
import com.badrqaba.core_ui.R
import com.badrqaba.core_ui.util.IS_DARK_THEME

class AuthenticationRobot(composeTestRule: ComposeTestRule) : Robot(composeTestRule) {
    fun assertUserCanLogin() {
        assertCanTapOnLoginButton()

        val emailTextField = hasEditableLabel(stringResource(R.string.lbl_email))
        val passwordTextField = hasEditableLabel(stringResource(R.string.lbl_password))


        waitUntil(
            timeoutMillis = 5_000,
            condition = {
                withNode(
                    matcher = emailTextField
                ).isDisplayed()
            }
        )

        insertText(matcher = emailTextField, text = MockData.MOCK_EMAIL)
        insertText(matcher = passwordTextField, text = MockData.MOCK_PASSWORD)

        withNode(
            matcher = hasContentDescription(
                stringResource(R.string.cd_button_login)
            ),
            useUnmergedTree = true
        ).assertHasClickAction().performClick()

        waitUntil(
            timeoutMillis = 5_000,
            condition = {
                withNode(
                    matcher = emailTextField
                ).isNotDisplayed()
            }
        )

        withNode(
            matcher = hasContentDescription(
                stringResource(R.string.cd_toolbar_user_menu)
            )
        ).assertIsDisplayed()
    }

    private fun assertCanTapOnLoginButton() {
        withNode(
            matcher = hasContentDescription(
                stringResource(R.string.cd_toolbar_login)
            ),
            useUnmergedTree = true
        ).assertHasClickAction().performClick()
    }

    fun assertUserCanRegister() {
        val name = "some user name"

        assertCanTapOnLoginButton()

        withNode(
            matcher = hasContentDescription(
                stringResource(R.string.cd_register_now)
            )
        ).assertHasClickAction().performClick()

        val nameTextField = hasEditableLabel(stringResource(R.string.lbl_first_and_last_name))
        val emailTextField = hasEditableLabel(stringResource(R.string.lbl_email))
        val passwordField = hasEditableLabel(stringResource(R.string.lbl_password))
        val passwordConfirmationField =
            hasEditableLabel(stringResource(R.string.lbl_confirm_password))

        waitUntil(
            timeoutMillis = 5_000,
            condition = {
                withNode(matcher = nameTextField).isDisplayed()
            }
        )

        insertText(
            matcher = nameTextField,
            text = name
        )

        insertText(
            matcher = emailTextField,
            text = MockData.MOCK_EMAIL
        )

        insertText(
            matcher = passwordField,
            text = MockData.MOCK_PASSWORD
        )

        insertText(
            matcher = passwordConfirmationField,
            text = MockData.MOCK_PASSWORD
        )

        withNode(
            matcher = hasContentDescription(stringResource(R.string.cd_button_register)),
            useUnmergedTree = true
        ).assertHasClickAction().performClick()

        waitUntil(
            timeoutMillis = 5_000,
            condition = {
                withNode(
                    matcher = hasContentDescription(
                        stringResource(
                            R.string.txt_registered,
                            name
                        )
                    )
                ).isDisplayed()
            }
        )
    }

    fun assertCanClickOnWishlistManagerMenuItem() {
        withNode(
            matcher = hasContentDescription(
                stringResource(R.string.cd_toolbar_user_menu)
            )
        ).assertHasClickAction().performClick()

        val wishlistManagerUserMenuItem = withNode(
            matcher = hasContentDescription(
                stringResource(R.string.cd_wishlist_user_menu)
            ),
            useUnmergedTree = true
        )

        waitUntil(timeoutMillis = 5_0000) {
            wishlistManagerUserMenuItem.isDisplayed()
        }

        wishlistManagerUserMenuItem
            .assertHasClickAction()
            .performClick()
    }

    fun assertCanSwitchTheme() {
        withNode(
            matcher = hasContentDescription(
                stringResource(
                    R.string.cd_toolbar_user_menu
                )
            )
        ).assertHasClickAction().performClick()

        val switchThemeMenu = withNode(
            matcher = hasContentDescription(
                stringResource(
                    R.string.cd_switch_theme_user_menu
                )
            ),
            useUnmergedTree = true
        )

        val switchThemeToggle = withNode(
            matcher = hasContentDescription(
                stringResource(
                    R.string.cd_theme_switch
                )
            ),
            useUnmergedTree = true
        )

        waitUntil(
            timeoutMillis = 5_000,
            condition = {
                switchThemeMenu.isDisplayed()
            }
        )

        switchThemeToggle
            .assertHasClickAction()
            .performClick()

        waitUntil(
            timeoutMillis = 5_000,
            condition = {
                withNode(
                    matcher = hasContentDescription(
                        stringResource(
                            R.string.cd_unchecked_icon
                        )
                    )
                ).isDisplayed()
            }
        )

        assertIsDisplayed(
            matcher = hasContentDescription(IS_DARK_THEME)
        )
    }

    fun assertCanLogout() {
        val userMenu = withNode(
            matcher = hasContentDescription(
                stringResource(R.string.cd_toolbar_user_menu)
            )
        )

        val loginButton = withNode(
            matcher = hasContentDescription(
                stringResource(R.string.cd_toolbar_login)
            ),
            useUnmergedTree = true
        )

        userMenu
            .assertHasClickAction()
            .performClick()

        val logoutUserMenu = withNode(
            matcher = hasContentDescription(
                stringResource(R.string.cd_logout_menu_item)
            ),
            useUnmergedTree = true
        )

        waitUntil(timeoutMillis = 5_0000) {
            logoutUserMenu.isDisplayed()
        }

        logoutUserMenu
            .assertHasClickAction()
            .performClick()

        waitUntil(
            timeoutMillis = 5_000,
            condition = {
                loginButton.isDisplayed()
            }
        )
    }
}