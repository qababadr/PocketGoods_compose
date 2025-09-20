package com.badrqaba.core_ui.component.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.badrqaba.core_ui.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun NetworkCarousel(
    images: List<String>,
    modifier: Modifier = Modifier,
    isAutoPlay: Boolean = true,
    timeout: Long = 4000L,
    shape: Shape = MaterialTheme.shapes.small,
    contentScale: ContentScale = ContentScale.Crop,
    testTag: String = ""
) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { images.count() }
    )

    val scope = rememberCoroutineScope()

    LaunchedEffect(isAutoPlay) {
        if (isAutoPlay) {
            while (true) {
                delay(timeout)
                pagerState.animateScrollToPage(
                    page = if (pagerState.currentPage == images.count() - 1) 0
                    else (pagerState.currentPage + 1)
                )
            }
        }
    }

    Box(modifier = modifier) {
        HorizontalPager(
            state = pagerState,
            key = { images[it] },
            modifier = Modifier
                .fillMaxSize()
                .clip(shape = shape)
                .testTag(testTag)
        ) { index ->
            NetworkImage(
                data = images[index],
                contentDescription = "image ${images.elementAt(index)}",
                contentScale = contentScale,
                modifier = Modifier.fillMaxSize(),
                crossFade = 1000,
                loadingComponent = {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                errorComponent = {
                    Image(
                        painter = painterResource(id = R.drawable.picture_image_svgrepo_com),
                        contentDescription = "",
                        modifier = Modifier.requiredWidth(width = 60.dp)
                    )
                }
            )
        }
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            images.forEachIndexed { index, _ ->
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(8.dp)
                        .background(
                            if (index == pagerState.currentPage) Color.Gray
                            else Color.LightGray
                        )
                        .clickable {
                            scope.launch {
                                pagerState.animateScrollToPage(
                                    images.indexOf(images.elementAt(index))
                                )
                            }
                        }
                )
            }
        }
    }
}