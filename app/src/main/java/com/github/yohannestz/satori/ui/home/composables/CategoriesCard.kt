package com.github.yohannestz.satori.ui.home.composables

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.yohannestz.satori.R

@Composable
fun CategoriesCard(
    text: String,
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    Card(
        onClick = onClick,
        modifier = modifier.padding(start = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .height(40.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = text,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(18.dp)
            )

            Text(
                text = text,
                fontSize = 15.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                lineHeight = 15.sp
            )
        }
    }
}

@Preview
@Composable
private fun CategoriesCardPreview() {
    CategoriesCard(
        text = "What a long text",
        icon = R.drawable.ic_round_collections_bookmark_24
    ) {}
}