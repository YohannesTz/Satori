package com.github.yohannestz.satori.ui.about.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.github.yohannestz.satori.R
import com.github.yohannestz.satori.data.model.contributors.Contributor

@Composable
fun ContributorsItem(
    item: Contributor,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.padding(8.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = item.avatarUrl,
            contentDescription = "avatar",
            contentScale = ContentScale.Crop,
            placeholder = ColorPainter(MaterialTheme.colorScheme.outline),
            error = ColorPainter(MaterialTheme.colorScheme.outline),
            fallback = ColorPainter(MaterialTheme.colorScheme.outline),
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
        )

        Column {
            Text(
                text = item.login,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                lineHeight = 22.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Text(
                text = "${item.contributions} ${stringResource(R.string.contributions)}",
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            modifier = Modifier.size(32.dp),
            painter = painterResource(
                id = R.drawable.ic_link_outward_24
            ),
            contentDescription = null
        )
    }
}