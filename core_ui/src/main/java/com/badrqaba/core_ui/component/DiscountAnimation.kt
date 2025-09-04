package com.badrqaba.core_ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lottiefiles.dotlottie.core.compose.ui.DotLottieAnimation
import com.lottiefiles.dotlottie.core.util.DotLottieSource
import com.lottiefiles.dotlottie.core.widget.DotLottieAnimation

@Composable
fun DiscountAnimation(
    modifier: Modifier = Modifier,
    loop: Boolean = true,
) {
    DotLottieAnimation(
        source = DotLottieSource.Asset("discount_offers.json"),
        autoplay = true,
        loop = loop,
        useFrameInterpolation = false,
        modifier = modifier
    )
}