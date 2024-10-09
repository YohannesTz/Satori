package com.github.yohannestz.satori.ui.more

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.yohannestz.satori.R
import com.github.yohannestz.satori.ui.base.navigation.NavActionManager
import com.github.yohannestz.satori.ui.base.navigation.Route
import com.github.yohannestz.satori.ui.more.composable.MoreItem
import com.github.yohannestz.satori.ui.more.composable.SendFeedbackDialog
import com.github.yohannestz.satori.utils.Extensions.collapsable
import com.github.yohannestz.satori.utils.Extensions.openLink
import com.github.yohannestz.satori.utils.Extensions.showToast
import com.github.yohannestz.satori.utils.GOOGLE_BOOKS_URL

@Composable
fun MoreView(
    navActionManager: NavActionManager,
    topBarHeightPx: Float,
    topBarOffsetY: Animatable<Float, AnimationVector1D>,
    padding: PaddingValues,
) {
    MoreViewContent(
        navActionManager = navActionManager,
        topBarHeightPx = topBarHeightPx,
        topBarOffsetY = topBarOffsetY,
        padding = padding
    )
}

@Composable
private fun MoreViewContent (
    navActionManager: NavActionManager,
    topBarHeightPx: Float,
    topBarOffsetY: Animatable<Float, AnimationVector1D>,
    padding: PaddingValues,
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var openFeedbackDialog by remember { mutableStateOf(false) }

    if (openFeedbackDialog) {
        SendFeedbackDialog(
            onDismiss = {
                openFeedbackDialog = false
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .collapsable(
                state = scrollState,
                topBarHeightPx = topBarHeightPx,
                topBarOffsetY = topBarOffsetY,
            )
            .verticalScroll(scrollState)
            .padding(padding)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_satori_launcher),
            contentDescription = stringResource(R.string.app_name),
            modifier = Modifier
                .padding(vertical = 30.dp)
                .fillMaxWidth()
                .size(120.dp),
            tint = MaterialTheme.colorScheme.onSurface
        )

        HorizontalDivider()

        MoreItem(
            title = stringResource(R.string.about_google_books),
            subtitle = stringResource(R.string.about_google_books_subtitle),
            icon = R.drawable.ic_info_sided,
            onClick = {
                context.openLink(GOOGLE_BOOKS_URL)
            }
        )

        MoreItem(
            title = stringResource(R.string.my_library),
            subtitle = stringResource(R.string.my_library_subtitle),
            icon = R.drawable.ic_round_format_list_bulleted_24,
            onClick = {
                context.showToast(R.string.coming_soon)
            }
        )

        HorizontalDivider()

        MoreItem(
            title = stringResource(R.string.settings),
            icon = R.drawable.ic_round_settings_24,
            onClick = {
                navActionManager.navigateTo(Route.Settings)
            }
        )

        MoreItem(
            title = stringResource(R.string.about_us),
            icon = R.drawable.ic_round_info_24,
            onClick = {
                navActionManager.navigateTo(Route.About)
            }
        )

        MoreItem(
            title = stringResource(R.string.send_feedback),
            icon = R.drawable.ic_round_feedback_24,
            onClick = {
                openFeedbackDialog = true
            }
        )

        HorizontalDivider()

        MoreItem(
            title = stringResource(R.string.login),
            subtitle = stringResource(R.string.login_subtitle),
            icon = R.drawable.ic_round_power_settings_new_24,
            onClick = {
                context.showToast(R.string.coming_soon)
            }
        )
    }
}