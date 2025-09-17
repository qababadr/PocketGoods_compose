package com.badrqaba.wishlist_feature.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.badrqaba.core.domain.model.User
import com.badrqaba.core_ui.R
import com.badrqaba.core_ui.component.Modal
import com.badrqaba.core_ui.component.ProgressButton
import com.badrqaba.core_ui.component.WarningMessage
import com.badrqaba.core_ui.theme.PocketGoodsTheme
import com.badrqaba.core_ui.util.LOADING_INDICATOR
import com.badrqaba.core_ui.util.warningColor
import com.badrqaba.wishlist_feature.presentation.component.WishlistItemDeleteButton
import com.badrqaba.wishlist_feature.presentation.component.WishlistItemView
import kotlinx.coroutines.delay
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.badrqaba.core.data.mapper.toUser
import com.badrqaba.core.data.mapper.toWishlistItem
import com.badrqaba.core.util.api.mock.MockData
import kotlinx.coroutines.launch

@Composable
fun WishlistManagerScreen(
    state: WishlistManagerState,
    onEvent: (WishlistManagerScreenEvent) -> Unit,
    onUnAuthorized: () -> Unit,
    onProductClick: (Long) -> Unit,
    onDeleteSuccess: (String) -> Unit,
    onDeleteError: (String) -> Unit,
    authenticatedUser: User? = null,
) {

    val context = LocalContext.current

    var showWarning by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        authenticatedUser?.let {
            onEvent(WishlistManagerScreenEvent.GetWishlistItems(it.id))
        }
    }

    LaunchedEffect(authenticatedUser) {
        if (authenticatedUser == null) {
            onUnAuthorized()
        }
    }

    LaunchedEffect(state.isPageLoading, state.wishlist, state.error) {
        if (!state.isPageLoading
            && state.wishlist.isEmpty()
            && state.error == null
        ) {
            delay(1000)
            showWarning = true
        } else {
            showWarning = false
        }
    }

    AnimatedVisibility(visible = showWarning) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 12.dp)
                .clip(shape = MaterialTheme.shapes.large)
                .background(color = MaterialTheme.warningColor()),
            contentAlignment = Alignment.Center
        ) {
            WarningMessage(
                text = stringResource(R.string.txt_empty_wishlist),
                contentDescription = stringResource(R.string.txt_empty_wishlist),
                modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp)
            )
        }
    }

    if (state.isPageLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 26.dp),
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

            state.wishlist.isNotEmpty() -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.lbl_my_wishlist),
                        style = MaterialTheme.typography.titleLarge,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.padding(vertical = 26.dp)
                    )

                    state.wishlist.forEach { item ->
                        item.productDetail?.let { product ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 18.dp, horizontal = 12.dp),
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                WishlistItemView(
                                    product = product,
                                    onProductClick = { onProductClick(product.id) }
                                )

                                WishlistItemDeleteButton(
                                    onClick = {
                                        onEvent(
                                            WishlistManagerScreenEvent.ShowDeleteConfirmationModal(
                                                wishlistItem = item
                                            )
                                        )
                                    },
                                    testTag = context.getString(
                                        R.string.cd_open_delete_modal
                                    )
                                )
                            }
                        }
                    }

                    Modal(
                        isOpen = state.selectedWishlistItem != null,
                        title = {
                            Text(
                                text = stringResource(id = R.string.lbl_confirmation),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.padding(start = 6.dp)
                            )
                        },
                        headerColor = MaterialTheme.warningColor(),
                        onDismiss = {
                            onEvent(WishlistManagerScreenEvent.CloseDeleteConfirmationModal)
                        }
                    ) {
                        Column {
                            Text(
                                buildAnnotatedString {
                                    append(stringResource(id = R.string.txt_wishlist_deletion_confirmation_pt_1))

                                    withStyle(
                                        style = SpanStyle(fontWeight = FontWeight.Bold)
                                    ) {
                                        append(" ${state.selectedWishlistItem?.productDetail?.title} ")
                                    }

                                    append(stringResource(id = R.string.txt_wishlist_deletion_confirmation_pt_2))
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.padding(all = 12.dp)
                            )

                            HorizontalDivider(Modifier.padding(vertical = 12.dp, horizontal = 6.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp, end = 8.dp),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(
                                    colors = ButtonDefaults.buttonColors().copy(
                                        containerColor = MaterialTheme.colorScheme.secondary,
                                    ),
                                    onClick = {
                                        onEvent(WishlistManagerScreenEvent.CloseDeleteConfirmationModal)
                                    },
                                    shape = MaterialTheme.shapes.medium
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.lbl_close),
                                        style = MaterialTheme.typography.titleSmall,
                                        color = MaterialTheme.colorScheme.onSecondary,
                                    )
                                }

                                Spacer(modifier = Modifier.width(width = 12.dp))

                                ProgressButton(
                                    isLoading = state.isDeleting,
                                    modifier = Modifier
                                        .wrapContentWidth(align = Alignment.CenterHorizontally)
                                        .semantics {
                                            contentDescription = context.getString(
                                                R.string.cd_delete_wishlist_item,
                                                "${state.selectedWishlistItem?.productId}"
                                            )
                                        },
                                    shape = MaterialTheme.shapes.small,
                                    buttonColors = ButtonDefaults.buttonColors().copy(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        contentColor = MaterialTheme.colorScheme.onPrimary,
                                        disabledContentColor = MaterialTheme.colorScheme.onPrimary,
                                        disabledContainerColor = MaterialTheme.colorScheme.primary.copy(
                                            alpha = 0.5f
                                        )
                                    ),
                                    onClick = {
                                        authenticatedUser?.let { user ->
                                            onEvent(
                                                WishlistManagerScreenEvent.DeleteWishlistItem(
                                                    userId = user.id,
                                                    onSuccess = { productTitle ->
                                                        onDeleteSuccess(productTitle)
                                                        onEvent(WishlistManagerScreenEvent.CloseDeleteConfirmationModal)
                                                        onEvent(
                                                            WishlistManagerScreenEvent.GetWishlistItems(
                                                                user.id
                                                            )
                                                        )
                                                    },
                                                    onError = { _, productTitle ->
                                                        onDeleteError(productTitle)
                                                    }
                                                )
                                            )
                                        }
                                    }
                                ) { contentColor ->
                                    Text(
                                        text = stringResource(id = R.string.lbl_delete),
                                        color = contentColor,
                                        style = MaterialTheme.typography.titleSmall,
                                        modifier = Modifier.padding(horizontal = 6.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun WishlistManagerScreenPreview() {

    var state by remember { mutableStateOf(WishlistManagerState()) }
    val scope = rememberCoroutineScope()

    PocketGoodsTheme {
        Box(modifier = Modifier.padding(top = 100.dp)) {
            WishlistManagerScreen(
                state = state,
                onEvent = { event ->
                    when (event) {
                        is WishlistManagerScreenEvent.CloseDeleteConfirmationModal ->
                            state = state.copy(
                                selectedWishlistItem = null
                            )

                        is WishlistManagerScreenEvent.DeleteWishlistItem -> {
                            state.selectedWishlistItem?.let { selectedItem ->
                                scope.launch {
                                    state = state.copy(
                                        isDeleting = true
                                    )

                                    delay(1500)

                                    state = state.copy(
                                        isDeleting = false,
                                        wishlist = state
                                            .wishlist
                                            .filter { it.productId != selectedItem.productId },
                                        selectedWishlistItem = null
                                    )
                                }
                            }
                        }

                        is WishlistManagerScreenEvent.GetWishlistItems -> {
                            scope.launch {
                                state = state.copy(
                                    isPageLoading = true
                                )

                                delay(1500)

                                state = state.copy(
                                    isPageLoading = false,
                                    wishlist = MockData
                                        .userDTO
                                        .wishlist
                                        .map { it.toWishlistItem() },
                                )
                            }
                        }

                        is WishlistManagerScreenEvent.ShowDeleteConfirmationModal -> {
                            state = state.copy(
                                selectedWishlistItem = event.wishlistItem
                            )
                        }
                    }
                },
                onUnAuthorized = { },
                onProductClick = { _ -> },
                onDeleteSuccess = { _ -> },
                onDeleteError = { _ -> },
                authenticatedUser = MockData.userDTO.toUser()
            )
        }
    }
}