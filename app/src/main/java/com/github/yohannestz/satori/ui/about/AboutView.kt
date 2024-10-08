package com.github.yohannestz.satori.ui.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.yohannestz.satori.R
import com.github.yohannestz.satori.ui.about.composable.ContributorsItem
import com.github.yohannestz.satori.ui.base.navigation.NavActionManager
import com.github.yohannestz.satori.ui.composables.DefaultScaffoldWithTopAppBar
import com.github.yohannestz.satori.ui.composables.preferences.PlainPreferenceView
import com.github.yohannestz.satori.ui.settings.composables.SettingsTitle
import com.github.yohannestz.satori.utils.Extensions.openLink
import com.github.yohannestz.satori.utils.Extensions.versionCode
import com.github.yohannestz.satori.utils.Extensions.versionName
import com.github.yohannestz.satori.utils.GITHUB_RELEASES_URL
import com.github.yohannestz.satori.utils.GITHUB_REPOSITORY_URL
import org.koin.androidx.compose.koinViewModel

@Composable
fun AboutView(
    navActionManager: NavActionManager
) {
    val viewModel: AboutViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AboutViewContent(
        navActionManager = navActionManager,
        uiState = uiState
    )
}

@Composable
private fun AboutViewContent(
    navActionManager: NavActionManager,
    uiState: AboutUiState,
) {
    val context = LocalContext.current
    val versionName = context.versionName
    val versionCode = context.versionCode

    DefaultScaffoldWithTopAppBar(
        title = stringResource(R.string.about),
        navigateBack = navActionManager::goBack
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            PlainPreferenceView(
                title = stringResource(R.string.version),
                subtitle = "$versionName ($versionCode)",
                icon = R.drawable.ic_round_info_24,
                onClick = {}
            )

            PlainPreferenceView(
                title = stringResource(R.string.github_repository),
                subtitle = stringResource(R.string.github_repository_subtitle),
                icon = R.drawable.ic_github_icon,
                onClick = {
                    context.openLink(GITHUB_REPOSITORY_URL)
                }
            )

            PlainPreferenceView(
                title = stringResource(R.string.github_repository_releases),
                subtitle = stringResource(R.string.github_repository_releases_subtitle),
                icon = R.drawable.ic_round_campaign_24,
                onClick = {
                    context.openLink(GITHUB_RELEASES_URL)
                }
            )

            SettingsTitle(stringResource(R.string.contributors))
            if (uiState.isLoading) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp)
                    )
                }
            } else {
                uiState.contributors?.forEach { contributorItem ->
                    ContributorsItem(
                        item = contributorItem,
                        onClick = {
                            context.openLink(contributorItem.htmlUrl)
                        }
                    )
                }
            }
        }
    }
}