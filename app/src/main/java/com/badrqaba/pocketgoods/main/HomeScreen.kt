package com.badrqaba.pocketgoods.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.badrqaba.core.domain.model.User
import com.badrqaba.core_ui.R
import com.badrqaba.core_ui.component.DiscountAnimation
import com.badrqaba.pocketgoods.util.LocalIsTestMode
import com.badrqaba.product_feature.presentation.screen.ProductEvent
import com.badrqaba.product_feature.presentation.screen.ProductState
import com.badrqaba.product_feature.presentation.screen.ProductsFragment

@Composable
fun HomeScreen(
    productState: ProductState,
    shouldPaginate: Boolean,
    onProductEvent: (ProductEvent) -> Unit,
    onViewProduct: (Long) -> Unit,
    onToggleWishlist: (productId: Long, productTitle: String) -> Unit,
    authenticatedUser: User? = null,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            DiscountAnimation(
                modifier = Modifier
                    .requiredSize(size = 160.dp)
                    .padding(vertical = 20.dp),
                loop = !LocalIsTestMode.current
            )
        }
        Text(
            text = stringResource(id = R.string.lbl_discount),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .padding(bottom = 12.dp)
        )

        ProductsFragment(
            state = productState,
            onEvent = onProductEvent,
            authenticatedUser = authenticatedUser,
            onToggleWishlist = onToggleWishlist,
            onViewProduct = onViewProduct,
            shouldPaginate = shouldPaginate,
        )
    }
}