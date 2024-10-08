package com.github.yohannestz.satori.ui.more.composable

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MoreItem(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int? = null,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(
                painter = painterResource(icon),
                contentDescription = "",
                modifier = Modifier.padding(16.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        } else {
            Spacer(
                modifier = Modifier
                    .padding(16.dp)
                    .size(24.dp)
            )
        }

        Column(
            modifier = if (subtitle != null)
                Modifier.padding(16.dp)
            else Modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (subtitle != null) {
                Text(
                    text = subtitle,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp
                )
            }
        }
    }
}