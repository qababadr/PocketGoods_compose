package com.badrqaba.product_feature.presentation.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.badrqaba.core.data.mapper.toProductPreview
import com.badrqaba.core.domain.model.ProductPreview
import com.badrqaba.core.util.api.mock.MockData
import com.badrqaba.core_ui.R
import com.badrqaba.core_ui.theme.PocketGoodsTheme
import com.badrqaba.core_ui.util.SEARCH_INPUT_LOADING_INDICATOR
import com.badrqaba.core_ui.util.screenHeight
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SearchInput(
    query: String,
    searchLabel: String,
    onSearch: (String) -> Unit,
    search: () -> Unit,
    onClearQuery: () -> Unit,
    onSuggestedProductClick: (Long) -> Unit,
    focusManager: FocusManager,
    modifier: Modifier = Modifier,
    isProcessing: Boolean = false,
    closeButtonContentDescription: String = "",
    textFieldContentDescription: String = "",
    searchButtonContentDescription: String = "",
    suggestedProducts: List<ProductPreview> = emptyList()
) {
    var isExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var hasFocus by remember { mutableStateOf(false) }
    val targetWidthFraction = if (hasFocus) 0.75f else 0.45f
    val animateWidthFraction by animateFloatAsState(targetValue = targetWidthFraction)

    Box(modifier = modifier) {
        BasicTextField(
            value = query,
            onValueChange = {
                onSearch(it)
                isExpanded = it.isNotEmpty()
            },
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onBackground
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    focusManager.clearFocus()
                    search()
                }
            ),
            modifier = Modifier
                .padding(start = 4.dp)
                .fillMaxWidth(animateWidthFraction)
                .onFocusEvent { hasFocus = it.hasFocus }
                .clickable {
                    if (!isExpanded && query.isNotEmpty()) {
                        isExpanded = true
                    }
                }
                .semantics {
                    contentDescription = textFieldContentDescription
                },
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .height(height = 40.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(50.dp)
                        )
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (query.isEmpty()) {
                            Text(
                                text = searchLabel,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                        innerTextField()
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        if (query.isNotEmpty()) {
                            if (isProcessing) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .requiredSize(20.dp)
                                        .testTag(
                                        SEARCH_INPUT_LOADING_INDICATOR
                                    ),
                                    strokeWidth = 2.5.dp
                                )
                            } else {
                                Icon(
                                    painter = painterResource(id = R.drawable.close_circle),
                                    contentDescription = closeButtonContentDescription,
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier
                                        .requiredSize(18.dp)
                                        .clickable {
                                            onClearQuery()
                                            isExpanded = false
                                        }
                                )
                            }
                        }
                        Icon(
                            painter = painterResource(id = R.drawable.magnify),
                            contentDescription = searchButtonContentDescription,
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .requiredSize(18.dp)
                                .clickable {
                                    isExpanded = false
                                    search()
                                }
                        )

                    }
                }
            }
        )

        if (isExpanded
            && suggestedProducts.isNotEmpty()
            && query.isNotEmpty()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .requiredHeight(height = screenHeight() * 0.4f)
                    .padding(top = 45.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(MaterialTheme.colorScheme.surface)
                    .verticalScroll(rememberScrollState())
                    .zIndex(10f)
                    .testTag(stringResource(R.string.cd_search_input_list_test_tag)),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                suggestedProducts.forEach { product ->
                    SearchSuggestionItem(
                        modifier = Modifier
                            .semantics {
                                contentDescription =
                                    context.getString(
                                        R.string.cd_suggested_product,
                                        product.title
                                    )
                            },
                        thumbnail = product.thumbnail,
                        title = product.title,
                        category = product.category,
                        onClick = {
                            isExpanded = false
                            onSuggestedProductClick(product.id)
                        }
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Preview
@Composable
private fun SearchInputPreview() {
    PocketGoodsTheme {
        var query by remember { mutableStateOf("") }
        var isProcessing by remember { mutableStateOf(false) }
        val focusManager = LocalFocusManager.current
        val scope = rememberCoroutineScope()
        Column (
            modifier = Modifier
                .padding(all = 60.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            SearchInput(
                modifier = Modifier.padding(top = 60.dp),
                query = query,
                isProcessing = isProcessing,
                searchLabel = "Search",
                onSearch = {
                    query = it
                    scope.launch {
                        isProcessing = true
                        delay(1000)
                        isProcessing = false
                    }
                },
                search = {},
                onClearQuery = { query = "" },
                onSuggestedProductClick = {},
                focusManager = focusManager,
                closeButtonContentDescription = "",
                textFieldContentDescription = "",
                suggestedProducts = MockData
                    .suggestedProducts(query = query)
                    .data
                    .map { it.toProductPreview() }
            )

            SearchInput(
                query = query,
                isProcessing = isProcessing,
                searchLabel = "Search",
                onSearch = {
                    query = it
                    scope.launch {
                        isProcessing = true
                        delay(1000)
                        isProcessing = false
                    }
                },
                search = {},
                onClearQuery = { query = "" },
                onSuggestedProductClick = {},
                focusManager = focusManager,
                closeButtonContentDescription = "",
                textFieldContentDescription = "",
                suggestedProducts = MockData
                    .suggestedProducts(query = query)
                    .data
                    .map { it.toProductPreview() }
            )
        }
    }
}