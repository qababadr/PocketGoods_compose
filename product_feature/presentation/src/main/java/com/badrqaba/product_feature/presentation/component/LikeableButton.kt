package com.badrqaba.product_feature.presentation.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.badrqaba.core_ui.R
import com.badrqaba.core_ui.theme.PocketGoodsTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

@Composable
fun LikeableButton(
    isLiked: Boolean,
    onLikeClicked: () -> Unit,
    modifier: Modifier = Modifier,
    iconSize: Dp = 26.dp,
    contentDescription: String = ""
) {

    var didPulse by rememberSaveable { mutableStateOf(false) }
    val targetWidthFraction = if (didPulse) iconSize + 10.dp else iconSize
    val animateDpAsState by animateDpAsState(targetValue = targetWidthFraction)

    LaunchedEffect(didPulse) {
        delay(200)
        didPulse = false
    }

    IconButton(
        onClick = {
            runBlocking {
                didPulse = true
                onLikeClicked()
            }
        },
        modifier = modifier
            .padding(all = 6.dp)
            .clip(shape = RoundedCornerShape(size = 25.dp))
            .background(color = MaterialTheme.colorScheme.surface)
    ) {
        Icon(
            painter = painterResource(
                id = if (isLiked) R.drawable.heart
                else R.drawable.heart_outlined,
            ),
            contentDescription = contentDescription,
            modifier = Modifier.requiredSize(animateDpAsState),
            tint = if (isLiked) MaterialTheme.colorScheme.error
            else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview
@Composable
private fun LikeableButtonPreview() {
    var isLiked by rememberSaveable { mutableStateOf(false) }
    PocketGoodsTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LikeableButton(
                isLiked = isLiked,
                onLikeClicked = { isLiked = !isLiked }
            )
        }
    }
}