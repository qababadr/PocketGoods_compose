package com.badrqaba.product_feature.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.badrqaba.core.domain.model.ProductPreview
import com.badrqaba.core.domain.model.User
import com.badrqaba.core_ui.R
import com.badrqaba.core_ui.component.GridLayout
import com.badrqaba.core_ui.component.WarningMessage
import com.badrqaba.core_ui.util.LOADING_INDICATOR
import com.badrqaba.core_ui.util.media_query.ScreenSize
import com.badrqaba.core_ui.util.media_query.getScreenSize
import com.badrqaba.core_ui.util.warningColor
import com.badrqaba.product_feature.presentation.component.ProductCard

@Composable
fun SearchResultScreen(
    state: ProductState,
    onEvent: (ProductEvent) -> Unit,
    onViewProduct: (Long) -> Unit,
    onToggleWishlist: (productId: Long, productTitle: String) -> Unit,
    authenticatedUser: User? = null,
    shouldPaginate: Boolean = false,
) {

    val screenSize = getScreenSize()
    val context = LocalContext.current

    val perRow = when (screenSize) {
        ScreenSize.SMALL -> 1
        ScreenSize.MEDIUM -> 2
        ScreenSize.LARGE -> 3
        ScreenSize.XLARGE -> 3
    }

    LaunchedEffect(shouldPaginate) {
        if (shouldPaginate && !state.isPageLoading) {
            onEvent(ProductEvent.OnNextPage)
            onEvent(ProductEvent.SearchProducts)
        }
    }

    LaunchedEffect(Unit) {
        onEvent(ProductEvent.SearchProducts)
    }

    if (state.isPageLoading && state.searchResults.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(modifier = Modifier.testTag(LOADING_INDICATOR))
        }
    } else {
        when {
            state.error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 12.dp)
                        .clip(shape = MaterialTheme.shapes.large)
                        .background(color = MaterialTheme.warningColor()),
                    contentAlignment = Alignment.Center
                ) {
                    WarningMessage(
                        text = stringResource(id = R.string.err_fetching_data),
                        contentDescription = stringResource(R.string.cd_error_fetching),
                        modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp)
                    )
                }
            }

            state.searchResults.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 12.dp)
                        .clip(shape = MaterialTheme.shapes.large)
                        .background(color = MaterialTheme.warningColor()),
                    contentAlignment = Alignment.Center
                ) {
                    WarningMessage(
                        text = stringResource(id = R.string.txt_empty_product_list),
                        contentDescription = stringResource(R.string.txt_empty_product_list),
                        modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp)
                    )
                }
            }

            else -> {
                Column {
                    GridLayout(
                        columns = perRow,
                        items = state.searchResults ,
                        modifier = Modifier.fillMaxSize()
                    ) { productPreview: ProductPreview, _ ->
                        ProductCard(
                            product = productPreview,
                            isAuthenticated = authenticatedUser != null,
                            inWishlist = authenticatedUser
                                ?.wishlist
                                ?.any { it.productId == productPreview.id }
                                ?: false,
                            onViewProduct = onViewProduct,
                            onToggleWishlist = onToggleWishlist,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                                .semantics {
                                    contentDescription = context.getString(
                                        R.string.cd_product_card,
                                        productPreview.title
                                    )
                                }
                        )
                    }

                    if (state.isPageLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(modifier = Modifier.testTag(LOADING_INDICATOR))
                        }
                    }
                }
            }
        }
    }
}