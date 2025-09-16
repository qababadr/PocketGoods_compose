package com.badrqaba.product_feature.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.badrqaba.core_ui.R

@Composable
fun ToggleWishlistButton(
    onToggleWishlist: () -> Unit,
    inWishlist: Boolean,
    modifier: Modifier = Modifier
) {
    Button(
        colors = ButtonDefaults.buttonColors().copy(
            contentColor = MaterialTheme.colorScheme.primary
        ),
        onClick = onToggleWishlist,
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(
                    id = if (inWishlist) R.drawable.heart_minus else R.drawable.heart_plus
                ),
                contentDescription = stringResource(
                    id = if (inWishlist) R.string.cd_remove_from_wishlist else R.string.cd_add_to_wishlist
                ),
                tint = MaterialTheme.colorScheme.onPrimary
            )

            Text(
                text = stringResource(
                    id = if (inWishlist) R.string.lbl_remove_from_wishlist
                    else R.string.lbl_add_to_wishlist
                ).uppercase(),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Preview
@Composable
private fun ToggleWishlistButtonPreview() {

}