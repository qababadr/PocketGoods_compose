package com.badrqaba.authentication_feature.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.badrqaba.core_ui.R
import com.badrqaba.core_ui.component.ThemeSwitch
import com.badrqaba.core_ui.theme.PocketGoodsTheme
import com.badrqaba.core_ui.util.errorColor
import kotlinx.coroutines.launch

@Composable
fun UserMenu(
    onClick: () -> Unit,
    onWishlistClick: () -> Unit,
    onDarkThemeSwitched: (Boolean) -> Unit,
    onLogout: () -> Unit,
    initials: String,
    wishlistCount: Int,
    isDarkTheme: Boolean
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Box {
        Box(
            modifier = Modifier
                .shadow(
                    elevation = 6.dp,
                    shape = CircleShape
                )
                .clip(shape = CircleShape)
                .background(color = MaterialTheme.colorScheme.primary)
                .requiredSize(size = 40.dp)
                .clickable {
                    scope.launch {
                        onClick()
                        isExpanded = true
                    }
                }
                .semantics {
                    contentDescription = context.getString(
                        R.string.cd_toolbar_user_menu
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initials,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center,
            )
        }

        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            DropdownMenuItem(
                modifier = Modifier.semantics {
                    contentDescription = context.getString(R.string.cd_wishlist_user_menu)
                },
                text = {
                    Text(
                        text = stringResource(id = R.string.lbl_my_wishlist),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                },
                onClick = onWishlistClick,
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.book_heart),
                        contentDescription = "Wishlist",
                    )
                },
                trailingIcon = {
                    Badge(containerColor = MaterialTheme.errorColor()) {
                        Text(
                            text = "$wishlistCount",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(all = 4.dp)
                        )
                    }
                }
            )

            DropdownMenuItem(
                modifier = Modifier.semantics {
                    contentDescription = context.getString(R.string.cd_switch_theme_user_menu)
                },
                text = {
                    Text(
                        text = stringResource(id = R.string.lbl_switch_theme),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                },
                onClick = {},
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.palette),
                        contentDescription = "switch theme",
                    )
                },
                trailingIcon = {
                    ThemeSwitch(
                        isDarkTheme = isDarkTheme,
                        onThemeChange = onDarkThemeSwitched
                    )
                }
            )

            HorizontalDivider()

            DropdownMenuItem(
                modifier = Modifier.semantics {
                    contentDescription = context.getString(R.string.cd_logout_menu_item)
                },
                text = {
                    Text(
                        text = stringResource(id = R.string.lbl_logout),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                },
                onClick = onLogout,
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.logout_variant),
                        contentDescription = "Wishlist",
                    )
                },
            )

        }
    }
}

@Preview
@Composable
private fun UserMenuPreview() {
    PocketGoodsTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            UserMenu(
                onClick = {},
                onLogout = {},
                onWishlistClick = {},
                onDarkThemeSwitched = {},
                initials = "AB",
                wishlistCount = 0,
                isDarkTheme = false
            )
        }
    }
}