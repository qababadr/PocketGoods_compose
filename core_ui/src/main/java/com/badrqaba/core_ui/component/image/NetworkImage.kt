package com.badrqaba.core_ui.component.image

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil3.ImageLoader
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.placeholder
import coil3.request.transformations
import coil3.size.Scale
import coil3.size.Size
import coil3.transform.Transformation
import io.ktor.client.HttpClient

@Composable
fun NetworkImage(
    errorComponent: @Composable () -> Unit,
    loadingComponent: @Composable () -> Unit,
    data: Any,
    modifier: Modifier = Modifier,
    contentDescription: String = "",
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    placeholderDrawableRes: Int? = null,
    crossFade: Int? = null,
    transformations: List<Transformation>? = null,
    @DrawableRes errorResId: Int? = null
) {

    Box(modifier = modifier) {
        val context = LocalContext.current

        val imageLoader = remember {
            ImageLoader.Builder(context)
                .components {
                    add(
                        KtorNetworkFetcherFactory(
                            httpClient = {
                                HttpClient()
                            }
                        )
                    )
                }
                .build()
        }

        val imageRequest =
            remember(data, placeholderDrawableRes, errorResId, crossFade, transformations) {
                ImageRequest.Builder(context)
                    .data(data)
                    .size(Size.ORIGINAL)
                    .scale(Scale.FIT)
                    .apply {
                        placeholderDrawableRes?.let { placeholder(it) }
                        errorResId?.let { error(it) }
                        crossFade?.let { crossfade(it) }
                        transformations?.let { transformations(it) }
                    }
                    .build()
            }
        val painter = rememberAsyncImagePainter(model = imageRequest, imageLoader = imageLoader)

        val imageState by painter.state.collectAsState()

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (imageState is AsyncImagePainter.State.Error) {
                (imageState as AsyncImagePainter.State.Error).result.throwable.printStackTrace()
                errorComponent()
            }
            if (imageState is AsyncImagePainter.State.Loading) {
                loadingComponent()
            }
        }
        Image(
            painter = painter,
            modifier = Modifier.fillMaxSize(),
            contentDescription = contentDescription,
            contentScale = contentScale,
            alignment = alignment,
            alpha = alpha,
            colorFilter = colorFilter
        )
    }
}