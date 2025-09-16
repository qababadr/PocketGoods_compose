package com.badrqaba.product_feature.presentation.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.badrqaba.core.data.mapper.toProductPreview
import com.badrqaba.core.domain.model.ProductPreview
import com.badrqaba.core.util.api.mock.MockData
import com.badrqaba.core_ui.R
import com.badrqaba.core_ui.component.image.NetworkImage
import com.badrqaba.core_ui.theme.PocketGoodsTheme

@Composable
fun ProductCard(
    product: ProductPreview,
    isAuthenticated: Boolean,
    inWishlist: Boolean,
    onViewProduct: (Long) -> Unit,
    onToggleWishlist: (productId: Long, productTitle: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isInFrame by rememberSaveable { mutableStateOf(false) }
    val target = if (isInFrame) (-10).dp else 60.dp
    val animateDpAsState by animateDpAsState(targetValue = target)

    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box {
                if (isAuthenticated) {
                    LikeableButton(
                        isLiked = inWishlist,
                        iconSize = 30.dp,
                        onLikeClicked = {
                            onToggleWishlist(product.id, product.title)
                        },
                        modifier = Modifier
                            .align(alignment = Alignment.TopEnd)
                            .padding(vertical = 10.dp)
                            .offset(x = animateDpAsState)
                            .zIndex(1f)
                            .semantics {
                                contentDescription = context.getString(
                                    R.string.cd_toggle_wishlist, product.title
                                )
                            },
                        contentDescription = context.getString(
                            if (inWishlist) R.string.cd_product_in_wishlist
                            else R.string.cd_product_not_in_wishlist,
                            product.title
                        )

                    )
                }

                NetworkImage(
                    contentDescription = context.getString(
                        R.string.cd_image,
                        "${product.id} ${product.title}"
                    ),
                    contentScale = ContentScale.Crop,
                    data = product.thumbnail
                        ?: painterResource(id = R.drawable.select_your_item_shopping_svgrepo_com),
                    crossFade = 1000,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(fraction = 0.75f)
                        .clickable {
                            isInFrame = !isInFrame
                        }
                        .semantics {
                            contentDescription =
                                context.getString(
                                    R.string.cd_product_card_image_button,
                                    product.title
                                )
                        },
                    loadingComponent = { CircularProgressIndicator() },
                    errorComponent = {
                        Image(
                            painter = painterResource(id = R.drawable.picture_image_svgrepo_com),
                            contentDescription = "",
                            modifier = Modifier.requiredWidth(width = 60.dp)
                        )
                    }
                )
            }

            Column {
                Text(
                    text = product.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 8.dp)
                )

                Text(
                    text = stringResource(
                        id = R.string.lbl_price_value,
                        product.priceAsString
                    ),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 8.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = { onViewProduct(product.id) },
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.semantics {
                            contentDescription = context.getString(
                                R.string.cd_button_learn_more, product.title
                            )
                        }
                    ) {
                        Text(
                            text = stringResource(id = R.string.learn_more),
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                    }
                }
            }
        }
    }
}

@Preview()
@Composable
private fun ProductCardPreviewLight() {
    var inWishlist by remember { mutableStateOf(false) }
    PocketGoodsTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 60.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProductCard(
                product = MockData
                    .productsPaginationResponse
                    .data
                    .first()
                    .toProductPreview(),
                isAuthenticated = true,
                inWishlist = inWishlist,
                onViewProduct = {},
                onToggleWishlist = { _, _ -> inWishlist = !inWishlist },
                modifier = Modifier.padding(all = 10.dp)
            )
        }
    }
}