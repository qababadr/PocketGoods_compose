package com.badrqaba.product_feature.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.badrqaba.core.domain.model.User
import com.badrqaba.core.util.api.mock.MockData
import com.badrqaba.core_ui.R
import com.badrqaba.core_ui.component.WarningMessage
import com.badrqaba.core_ui.component.image.NetworkCarousel
import com.badrqaba.core_ui.theme.PocketGoodsTheme
import com.badrqaba.core_ui.util.LOADING_INDICATOR
import com.badrqaba.core_ui.util.PRODUCT_CAROUSEL_TAG
import com.badrqaba.core_ui.util.errorColor
import com.badrqaba.core_ui.util.screenHeight
import com.badrqaba.core_ui.util.successColor
import com.badrqaba.core_ui.util.warningColor
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.badrqaba.product_feature.presentation.component.ToggleWishlistButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ProductDetailScreen(
    state: ProductState,
    onToggleWishlist: (productId: Long, productTitle: String) -> Unit,
    onEvent: (ProductEvent) -> Unit,
    authenticatedUser: User? = null
) {

    LaunchedEffect(Unit) {
        onEvent(ProductEvent.LoadProduct)
    }

    if (state.isPageLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 26.dp), contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.testTag(LOADING_INDICATOR)
            )
        }
    } else {
        when {
            state.error != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
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
            }

            else -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    state.product?.let { product ->
                        NetworkCarousel(
                            shape = RectangleShape,
                            images = product.media.map { it.original },
                            isAutoPlay = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .requiredHeight(screenHeight() * 0.7f)
                                .semantics(mergeDescendants = true) {
                                    contentDescription = "$PRODUCT_CAROUSEL_TAG ${product.title}"
                                },
                            contentScale = ContentScale.Crop,
                            testTag = PRODUCT_CAROUSEL_TAG
                        )

                        Text(
                            text = product.title,
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleLarge,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(horizontal = 12.dp)
                                .fillMaxWidth()
                        )

                        Text(
                            buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold
                                    )
                                ) {
                                    append(stringResource(id = R.string.lbl_price) + " ")
                                }

                                append(
                                    stringResource(
                                        id = R.string.lbl_price_value,
                                        product.priceAsString
                                    )
                                )
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(start = 8.dp)
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = stringResource(id = R.string.lbl_description),
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = product.description,
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.bodyMedium,
                            )

                            Text(
                                text = buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            color = if (product.inStock) MaterialTheme.successColor()
                                            else MaterialTheme.errorColor(),
                                            fontWeight = FontWeight.Bold
                                        )
                                    ) {
                                        if (product.inStock) {
                                            append("${product.quantity}")
                                        }
                                    }
                                    if (product.inStock) {
                                        append(" " + stringResource(id = R.string.lbl_left_in_stock))
                                    } else {
                                        append(stringResource(id = R.string.lbl_out_of_stock))
                                    }
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = stringResource(id = R.string.lbl_category),
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = product.category,
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 18.dp, bottom = 36.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            ToggleWishlistButton(
                                inWishlist = authenticatedUser
                                    ?.wishlist
                                    ?.any { it.productId == product.id }
                                    ?: false,
                                onToggleWishlist = { onToggleWishlist(product.id, product.title) },
                                modifier = Modifier.fillMaxWidth(fraction = 0.6f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ProductDetailScreenAfterLoadingPreview() {

    var state by remember { mutableStateOf(ProductState()) }
    val scope = rememberCoroutineScope()

    PocketGoodsTheme {
        Column(
            modifier = Modifier
                .padding(top = 60.dp)
                .fillMaxSize()
                .verticalScroll(state = rememberScrollState())
        ) {
            ProductDetailScreen(
                state = state,
                onEvent = { event ->
                    when(event) {
                        is ProductEvent.LoadProduct -> {
                            scope.launch {
                                state = state.copy(
                                    isPageLoading = true
                                )

                                delay(1500)

                                state = state.copy(
                                    isPageLoading = false,
                                    product = MockData
                                        .sunGlassesProductResponse
                                        .data
                                )
                            }
                        }
                        else -> {}
                    }
                },
                onToggleWishlist = { _, _ -> }
            )
        }
    }
}