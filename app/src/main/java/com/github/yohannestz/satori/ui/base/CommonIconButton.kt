package com.github.yohannestz.satori.ui.base

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.github.yohannestz.satori.R
import com.github.yohannestz.satori.utils.Extensions.openShareSheet

fun singleClick(onClick: () -> Unit): () -> Unit {
    var latest = 0L
    return {
        val now = System.currentTimeMillis()
        if (now - latest >= 1000) {
            onClick()
            latest = now
        }
    }
}

@Composable
fun BackIconButton(
    onClick: () -> Unit
) {
    IconButton(onClick = singleClick(onClick)) {
        Icon(
            painter = painterResource(R.drawable.ic_round_arrow_back_24),
            contentDescription = stringResource(R.string.action_back)
        )
    }
}

@Composable
fun ShareIconButton(url: String) {
    val context = LocalContext.current
    IconButton(onClick = { context.openShareSheet(url) }) {
        Icon(
            painter = painterResource(R.drawable.ic_round_share_24),
            contentDescription = stringResource(R.string.share)
        )
    }
}