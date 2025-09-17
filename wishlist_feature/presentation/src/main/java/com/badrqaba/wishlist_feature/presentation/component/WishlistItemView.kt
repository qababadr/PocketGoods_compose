package com.badrqaba.wishlist_feature.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.badrqaba.core.domain.model.Product
import com.badrqaba.core.util.api.mock.MockData
import com.badrqaba.core_ui.R
import com.badrqaba.core_ui.component.image.NetworkImage
import com.badrqaba.core_ui.theme.PocketGoodsTheme
import com.badrqaba.core_ui.util.errorColor
import com.badrqaba.core_ui.util.successColor

@Composable
fun WishlistItemView(
    onProductClick: () -> Unit,
    product: Product,
) {
    Row(verticalAlignment = Alignment.Top) {
        NetworkImage(
            contentScale = ContentScale.Crop,
            data = product.media.first().preview,
            crossFade = 1000,
            modifier = Modifier
                .requiredSize(width = 120.dp, height = 100.dp)
                .clip(shape = MaterialTheme.shapes.small),
            loadingComponent = {
                CircularProgressIndicator()
            },
            errorComponent = {
                Image(
                    painter = painterResource(id = R.drawable.picture_image_svgrepo_com),
                    contentDescription = "",
                    modifier = Modifier.requiredWidth(width = 60.dp)
                )
            }
        )

        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(
                text = product.title,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable { onProductClick() },
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = stringResource(
                    id = R.string.lbl_price_value,
                    product.priceAsString
                ),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 20.dp, bottom = 8.dp)
            )

            Text(
                text = if (product.inStock) "${product.quantity} "
                        + stringResource(id = R.string.lbl_left_in_stock)
                else stringResource(id = R.string.lbl_out_of_stock),
                color = if (product.inStock)
                    MaterialTheme.successColor()
                else MaterialTheme.errorColor(),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Preview
@Composable
private fun WishlistItemPreview() {
    PocketGoodsTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            WishlistItemView(
                onProductClick = {},
                product = MockData.sunGlassesProductResponse.data
            )
        }
    }
}