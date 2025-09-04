package com.badrqaba.core_ui.component.image

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.content.MediaType.Companion.Image
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
import androidx.compose.ui.tooling.preview.Preview
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
    data: Any,
    modifier: Modifier = Modifier,
    errorComponent: @Composable (() -> Unit)? = null,
    loadingComponent: @Composable (() -> Unit)? = null,
    contentDescription: String? = null,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    placeHolderDrawableRes: Int? = null,
    crossFade: Int? = null,
    transformations: List<Transformation>? = null,
    @DrawableRes errorResId: Int? = null,
) {
    val context = LocalContext.current
    val imageLoader = remember {
        ImageLoader.Builder(context)
            .components {
                add(
                    KtorNetworkFetcherFactory(
                        httpClient = HttpClient()
                    )
                )
            }
            .build()
    }

    val imageRequest =
        remember(data, placeHolderDrawableRes, errorResId, crossFade, transformations) {

            ImageRequest.Builder(context)
                .data(data)
                .size(Size.ORIGINAL)
                .scale(Scale.FIT)
                .apply {
                    placeHolderDrawableRes?.let {
                        placeholder(it)
                    }
                    errorResId?.let {
                        error(it)
                    }
                    crossFade?.let {
                        crossfade(it)
                    }
                    transformations?.let {
                        transformations(it)
                    }

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
            errorComponent?.invoke()
        }
        if (imageState is AsyncImagePainter.State.Loading) {
            loadingComponent?.invoke()
        }
    }
    Image(
       painter = painter,
        modifier = modifier.fillMaxSize(),
        contentDescription = contentDescription,
        contentScale = contentScale,
        alignment = alignment,
        alpha = alpha,
        colorFilter = colorFilter
    )
}