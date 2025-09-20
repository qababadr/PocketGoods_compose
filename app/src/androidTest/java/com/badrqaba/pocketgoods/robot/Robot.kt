package com.badrqaba.pocketgoods.robot

import android.content.Context
import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.TestCase.assertTrue

abstract class Robot(private val composeRule: ComposeTestRule) {

    private val context: Context = InstrumentationRegistry
        .getInstrumentation()
        .targetContext

    fun scrollAndAssertIsDisplayed(
        scrollableContainerTag: String,
        matcher: SemanticsMatcher,
        useUnmergedTree: Boolean = false
    ) = composeRule
        .onNodeWithTag(scrollableContainerTag, useUnmergedTree)
        .performScrollToNode(matcher)
        .assertIsDisplayed()

    fun assertHasText(
        text: String,
        ignoreCase: Boolean = false,
        substring: Boolean = false,
        useUnmergedTree: Boolean = false
    ) = composeRule
        .onNode(
            hasText(text, ignoreCase = ignoreCase, substring = substring),
            useUnmergedTree = useUnmergedTree
        )
        .assertExists()


    fun assertHasContentDescription(
        contentDescription: String,
        ignoreCase: Boolean = false,
        substring: Boolean = false,
        useUnmergedTree: Boolean = false
    ) = composeRule
        .onNode(
            hasContentDescription(
                value = contentDescription,
                ignoreCase = ignoreCase,
                substring = substring
            ),
            useUnmergedTree = useUnmergedTree
        )
        .assertExists()

    fun stringResource(@StringRes id: Int) = context.resources.getString(id)

    fun stringResource(
        @StringRes id: Int,
        vararg formatArgs: Any
    ) = context.resources.getString(id, *formatArgs)

    fun scrollTo(
        scrollableContainerTag: String,
        matcher: SemanticsMatcher,
        useUnmergedTree: Boolean = false
    ): SemanticsNodeInteraction {
        return composeRule
            .onNodeWithTag(scrollableContainerTag, useUnmergedTree)
            .performScrollToNode(matcher)
    }

    fun assertIsDisplayed(
        matcher: SemanticsMatcher,
        useUnmergedTree: Boolean = false
    ) = composeRule
        .onNode(matcher, useUnmergedTree)
        .assertIsDisplayed()

    fun insertText(
        text: String,
        matcher: SemanticsMatcher,
        useUnmergedTree: Boolean = false
    ) = composeRule
        .onNode(matcher, useUnmergedTree)
        .performTextInput(text)

    fun hasEditableLabel(label: String): SemanticsMatcher {
        return SemanticsMatcher("hasEditableLabel(${label})") { semanticsNode ->
            val isEditable = semanticsNode
                .config
                .contains(SemanticsProperties.EditableText)

            val hasMatchingLabel = semanticsNode
                .config
                .getOrNull(SemanticsProperties.Text)
                ?.any { it.text == label }
                ?: false

            isEditable && hasMatchingLabel
        }
    }

    fun waitUntil(
        timeoutMillis: Long = 1_000,
        condition: () -> Boolean
    ) = composeRule
        .waitUntil(timeoutMillis = timeoutMillis) { condition() }

    fun withNode(
        matcher: SemanticsMatcher,
        useUnmergedTree: Boolean = false
    ) = composeRule.onNode(
        matcher = matcher,
        useUnmergedTree = useUnmergedTree
    )
}