package com.github.yohannestz.satori.ui.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.yohannestz.satori.R
import com.github.yohannestz.satori.ui.theme.SatoriTheme
import com.github.yohannestz.satori.utils.Extensions.toStringPositiveValueOrUnknown

@Composable
fun SmallScoreIndicator(
    score: Int?,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 14.sp,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_round_star_24),
            contentDescription = stringResource(R.string.mean_score),
            tint = MaterialTheme.colorScheme.outline
        )
        Text(
            text = score.toStringPositiveValueOrUnknown(),
            modifier = Modifier.padding(horizontal = 4.dp),
            color = MaterialTheme.colorScheme.outline,
            fontSize = fontSize
        )
    }
}

@Composable
fun SmallScoreIndicator(
    score: Float?,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 14.sp,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_round_star_24),
            contentDescription = stringResource(R.string.mean_score),
            tint = MaterialTheme.colorScheme.outline
        )
        Text(
            text = score.toStringPositiveValueOrUnknown(),
            modifier = Modifier.padding(horizontal = 4.dp),
            color = MaterialTheme.colorScheme.outline,
            fontSize = fontSize
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SmallScoreIndicatorPreview() {
    SatoriTheme {
        SmallScoreIndicator(score = 4.8f)
    }
}