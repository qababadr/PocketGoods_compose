package com.badrqaba.core_ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.badrqaba.core_ui.R
import com.badrqaba.core_ui.theme.PocketGoodsTheme

@Composable
fun ThemeSwitch(
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    val context = LocalContext.current
    Switch(
        modifier = Modifier
            .semantics {
                contentDescription = context.getString(R.string.cd_theme_switch)
            },
        checked = isDarkTheme,
        colors = SwitchDefaults.colors().copy(
            checkedIconColor = MaterialTheme.colorScheme.primary
        ),
        onCheckedChange = onThemeChange,
        thumbContent = {
            Icon(
                painter = if (isDarkTheme) painterResource(id = R.drawable.moon_waning_crescent) else
                    painterResource(id = R.drawable.white_balance_sunny),
                contentDescription = if (isDarkTheme) stringResource(R.string.cd_unchecked_icon) else
                    stringResource(R.string.cd_checked_icon)
            )
        }
    )
}

@Preview(name = "light theme switch")
@Composable
private fun ThemeSwitchPreviewLight() {
    PocketGoodsTheme {
        Box(modifier = Modifier.fillMaxSize()
            .padding(all = 26.dp)) {
            ThemeSwitch(
                isDarkTheme = false,
                onThemeChange = {}
            )
        }
    }
}

@Preview(name = "dark theme switch")
@Composable
private fun ThemeSwitchPreviewDark() {
    PocketGoodsTheme {
        Box(modifier = Modifier.fillMaxSize()
            .padding(all = 26.dp)) {
            ThemeSwitch(
                isDarkTheme = true,
                onThemeChange = {}
            )
        }
    }
}