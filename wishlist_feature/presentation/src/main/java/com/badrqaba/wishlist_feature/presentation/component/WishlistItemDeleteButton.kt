package com.badrqaba.wishlist_feature.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.badrqaba.core_ui.R
import com.badrqaba.core_ui.util.errorColor

@Composable
fun WishlistItemDeleteButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = "WishlistItemDeleteButton"
) {
    Surface(
        shape = RoundedCornerShape(size = 50.dp),
        tonalElevation = 4.dp,
        shadowElevation = 4.dp,
        color = MaterialTheme.errorColor(),
        modifier = modifier
            .clickable { onClick() }
            .semantics {
                contentDescription = testTag
            }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.delete),
            tint = MaterialTheme.colorScheme.onPrimary,
            contentDescription = stringResource(id = R.string.lbl_delete),
            modifier = Modifier.padding(all = 8.dp)
        )
    }
}