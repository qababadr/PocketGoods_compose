package com.badrqaba.core_ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.badrqaba.core_ui.R
import com.badrqaba.core_ui.theme.PocketGoodsTheme

@Composable
fun Header(
    modifier: Modifier = Modifier,
    onLogoClick: () -> Unit = {},
    searchInput: @Composable () -> Unit,
    userMenu: @Composable () -> Unit
) {
    Surface(
        tonalElevation = 4.dp,
        shadowElevation = 4.dp,
        shape = RectangleShape,
        modifier = Modifier.fillMaxSize()
    ) {
        Box {
            Image(
                painter = painterResource(R.drawable.blob_bg),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .padding(top = 16.dp, start = 60.dp)
                    .zIndex(20f)
            ) {
                searchInput()
            }

            Column(
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.full_logo),
                        contentDescription = stringResource(R.string.app_name),
                        modifier = Modifier
                            .requiredSize(size = 60.dp)
                            .clickable(onClick = onLogoClick)
                    )

                    userMenu()
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                ) {
                   TypingText(
                       modifier = Modifier.padding(horizontal = 12.dp)
                           .padding(top = 40.dp),
                       fullText = stringResource(R.string.header_Text),
                       typingSpeed = 40L,
                       textStyle = MaterialTheme.typography.bodyMedium,
                       color = Color.White
                   )
                }
            }
        }
    }
}

@Preview
@Composable
private fun HeaderPreview() {
    PocketGoodsTheme {
        var query by remember { mutableStateOf("") }
        Box(modifier = Modifier.padding(top = 100.dp)) {
            Header(
                searchInput = {
                    TextField(
                        value = query,
                        onValueChange = { query = it },
                        placeholder = { Text(text = "Search...") },
                        singleLine = true,
                        shape = RoundedCornerShape(25.dp),
                        trailingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.magnify),
                                contentDescription = null
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.LightGray,
                            unfocusedContainerColor = Color.LightGray,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        ),
                        modifier = Modifier.padding(16.dp)
                            .fillMaxWidth(0.8f)
                    )
                },
                userMenu = {
                    Surface(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(size = 50.dp)
                    ) {
                        Text(
                            text = "BQ",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 6.dp)
                        )
                    }
                }
            )
        }
    }
}