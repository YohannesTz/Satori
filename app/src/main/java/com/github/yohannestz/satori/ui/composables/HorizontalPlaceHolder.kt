package com.github.yohannestz.satori.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.yohannestz.satori.utils.Extensions.defaultPlaceholder

@Composable
fun HorizontalPlaceHolder() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(MEDIA_POSTER_SMALL_HEIGHT.dp)
    ) {
        Box(
            modifier = Modifier
                .size(
                    width = MEDIA_POSTER_SMALL_WIDTH.dp,
                    height = MEDIA_POSTER_SMALL_HEIGHT.dp
                )
                .clip(RoundedCornerShape(8.dp))
                .defaultPlaceholder(visible = true)
        )

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "This is a placeholder text",
                modifier = Modifier.defaultPlaceholder(visible = true),
                fontSize = 17.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "This is a placeholder",
                modifier = Modifier.defaultPlaceholder(visible = true)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Placeholder",
                modifier = Modifier.defaultPlaceholder(visible = true)
            )
        }
    }
}