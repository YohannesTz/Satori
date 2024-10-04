package com.github.yohannestz.satori.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.github.yohannestz.satori.ui.theme.SatoriTheme
import com.github.yohannestz.satori.ui.theme.banner_shadow_color

@Composable
fun TopBannerView(
    imageUrl: String?,
    modifier: Modifier = Modifier,
    fallBackColor: Color? = null,
    height: Dp
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .padding(bottom = 16.dp),
        contentAlignment = Alignment.TopStart
    ) {
        if (imageUrl != null) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "banner",
                placeholder = ColorPainter(MaterialTheme.colorScheme.outline),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .background(
                        color = fallBackColor ?: MaterialTheme.colorScheme.outline
                    )
                    .fillMaxSize()
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(banner_shadow_color, MaterialTheme.colorScheme.surface)
                    )
                )
        )
    }
}

@Preview
@Composable
fun TopBannerViewPreview() {
    SatoriTheme {
        Surface {
            TopBannerView(
                imageUrl = "https://picsum.photos/200",
                fallBackColor = MaterialTheme.colorScheme.secondary,
                height = 250.dp
            )
        }
    }
}