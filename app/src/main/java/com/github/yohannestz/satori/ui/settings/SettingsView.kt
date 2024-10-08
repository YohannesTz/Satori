package com.github.yohannestz.satori.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.yohannestz.satori.R
import com.github.yohannestz.satori.data.model.PrintType
import com.github.yohannestz.satori.data.model.ViewMode
import com.github.yohannestz.satori.ui.base.StartTab
import com.github.yohannestz.satori.ui.base.ThemeStyle
import com.github.yohannestz.satori.ui.base.navigation.NavActionManager
import com.github.yohannestz.satori.ui.composables.BackIconButton
import com.github.yohannestz.satori.ui.composables.DefaultScaffoldWithMediumTopAppBar
import com.github.yohannestz.satori.ui.composables.preferences.ListPreferenceView
import com.github.yohannestz.satori.ui.composables.preferences.SwitchPreferenceView
import com.github.yohannestz.satori.ui.settings.composables.SettingsTitle
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsView(
    navActionManager: NavActionManager
) {
    val viewModel: SettingsViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SettingsViewContent(
        uiState = uiState,
        event = viewModel,
        navActionManager = navActionManager
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsViewContent(
    uiState: SettingsUiState,
    event: SettingsEvent,
    navActionManager: NavActionManager
) {
    val topAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        rememberTopAppBarState()
    )

    DefaultScaffoldWithMediumTopAppBar(
        title = stringResource(R.string.settings),
        navigationIcon = {
            BackIconButton(onClick = navActionManager::goBack)
        },
        scrollBehavior = topAppBarScrollBehavior,
        contentWindowInsets = WindowInsets.systemBars
            .only(WindowInsetsSides.Horizontal),
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            scrolledContainerColor = TopAppBarDefaults.mediumTopAppBarColors().containerColor
        ),
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
                .verticalScroll(rememberScrollState())
        ) {
            SettingsTitle(text = stringResource(R.string.display))

            ListPreferenceView(
                title = stringResource(R.string.theme),
                entriesValues = mapOf(
                    ThemeStyle.FOLLOW_SYSTEM to R.string.follow_system,
                    ThemeStyle.LIGHT to R.string.light,
                    ThemeStyle.DARK to R.string.dark
                ),
                icon = R.drawable.ic_round_color_lens_24,
                value = uiState.theme,
                onValueChange = event::onThemeChanged
            )

            SwitchPreferenceView(
                title = stringResource(R.string.use_black_colors),
                value = uiState.useBlackColors,
                onValueChange = event::onUseBlackColors
            )

            ListPreferenceView(
                title = stringResource(R.string.default_start_tab),
                entriesValues = mapOf(
                    StartTab.LAST_USED to R.string.last_used,
                    StartTab.HOME to R.string.title_home,
                    StartTab.LATEST to R.string.title_latest,
                    StartTab.BOOKMARKS to R.string.title_bookmarks,
                    StartTab.MORE to R.string.title_settings
                ),
                value = uiState.startTab,
                icon = R.drawable.ic_round_home_24,
                onValueChange = { event.onStartTabChanged(it) }
            )

            Spacer(modifier = Modifier.height(48.dp))
            SettingsTitle(text = stringResource(R.string.content))

            ListPreferenceView(
                title = stringResource(R.string.volume_list_view_mode),
                entriesValues = mapOf(
                    ViewMode.LIST to R.string.list,
                    ViewMode.GRID to R.string.grid
                ),
                value = uiState.viewMode,
                icon = if (uiState.viewMode == ViewMode.LIST) R.drawable.ic_round_view_list_24 else R.drawable.ic_round_grid_view_24,
                onValueChange = event::onViewModeChanged
            )

            SwitchPreferenceView(
                title = stringResource(R.string.only_show_free),
                subtitle = stringResource(R.string.only_show_free_subtitle),
                value = uiState.onlyShowFreeContent,
                icon = R.drawable.ic_round_cloud_download_24,
                onValueChange = event::onOnlyShowFreeContentChanged
            )

            ListPreferenceView(
                title = stringResource(R.string.default_print_type),
                entriesValues = mapOf(
                    PrintType.ALL to R.string.all,
                    PrintType.BOOK to R.string.book,
                    PrintType.MAGAZINE to R.string.magazine
                ),
                value = uiState.defaultPrintType,

                onValueChange = event::onDefaultPrintTypeChanged
            )
        }
    }
}