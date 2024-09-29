package com.github.yohannestz.satori.utils

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.core.content.ContextCompat
import androidx.core.os.LocaleListCompat
import com.github.yohannestz.satori.R
import io.github.fornewid.placeholder.foundation.PlaceholderHighlight
import io.github.fornewid.placeholder.material3.fade
import io.github.fornewid.placeholder.material3.placeholder
import kotlinx.coroutines.launch
import kotlin.math.abs

object Extensions {
    fun Int.toStringPositiveValueOrUnknown(): String {
        return if (this > 0) this.toString() else UNKNOWN_CHAR
    }

    fun Float.toStringPositiveValueOrUnknown(): String {
        return if (this > 0) this.toString() else UNKNOWN_CHAR
    }

    fun Int?.toStringPositiveValueOrUnknown(): String {
        return if (this != null && this > 0) this.toString() else UNKNOWN_CHAR
    }

    fun Float?.toStringPositiveValueOrUnknown(): String {
        return if (this != null && this > 0) this.toString() else UNKNOWN_CHAR
    }

    fun Context.showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun Context.showToast(@StringRes stringRes: Int) {
        showToast(getString(stringRes))
    }

    fun Context.openAction(uri: String) {
        Intent(Intent.ACTION_VIEW, Uri.parse(uri)).apply {
            startActivity(this)
        }
    }

    fun Context.openShareSheet(url: String) {
        Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, url)
            type = "text/plain"
            startActivity(Intent.createChooser(this, null))
        }
    }

    fun Context.copyToClipBoard(text: String) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        clipboard?.setPrimaryClip(ClipData.newPlainText("title", text))
        showToast(getString(R.string.copied))
    }

    fun changeLocale(language: String) {
        val appLocale = if (language == "follow_system") LocaleListCompat.getEmptyLocaleList()
        else LocaleListCompat.forLanguageTags(language)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }

    /** Open link in Chrome Custom Tabs */
    fun Context.openCustomTab(url: String) {
        val colors = CustomTabColorSchemeParams.Builder()
            .setToolbarColor(ContextCompat.getColor(this, R.color.colorSatori))
            .build()
        CustomTabsIntent.Builder()
            .setDefaultColorSchemeParams(colors)
            .build()
            .apply {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                try {
                    launchUrl(this@openCustomTab, Uri.parse(url))
                } catch (e: ActivityNotFoundException) {
                    openLink(url)
                }
            }
    }

    /** Open external link by default browser or intent chooser */
    fun Context.openLink(url: String) {
        val uri = Uri.parse(url)
        Intent(Intent.ACTION_VIEW, uri).apply {
            val defaultBrowser =
                findBrowserIntentActivities(PackageManager.MATCH_DEFAULT_ONLY).firstOrNull()
            if (defaultBrowser != null) {
                try {
                    setPackage(defaultBrowser.activityInfo.packageName)
                    startActivity(this)
                } catch (e: ActivityNotFoundException) {
                    startActivity(Intent.createChooser(this, null))
                }
            } else {
                val browsers = findBrowserIntentActivities(PackageManager.MATCH_ALL)
                val intents = browsers.map {
                    Intent(Intent.ACTION_VIEW, uri).apply {
                        setPackage(it.activityInfo.packageName)
                    }
                }
                startActivity(
                    Intent.createChooser(this, null).apply {
                        putExtra(Intent.EXTRA_INITIAL_INTENTS, intents.toTypedArray())
                    }
                )
            }
        }
    }

    /** Finds all the browsers installed on the device */
    private fun Context.findBrowserIntentActivities(
        flags: Int = 0
    ): List<ResolveInfo> {
        val emptyBrowserIntent = Intent(Intent.ACTION_VIEW, Uri.fromParts("http", "", null))

        return packageManager
            .queryIntentActivitiesCompat(emptyBrowserIntent, flags)
            .filter { it.activityInfo.packageName != packageName }
            .sortedBy { it.priority }
    }

    /** Custom compat method until Google decides to make one */
    private fun PackageManager.queryIntentActivitiesCompat(intent: Intent, flags: Int = 0) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            queryIntentActivities(intent, PackageManager.ResolveInfoFlags.of(flags.toLong()))
        } else {
            queryIntentActivities(intent, flags)
        }

    @RequiresApi(Build.VERSION_CODES.S)
    fun Context.openByDefaultSettings() {
        try {
            // Samsung OneUI 4 bug can't open ACTION_APP_OPEN_BY_DEFAULT_SETTINGS
            val action = if (Build.MANUFACTURER.equals("samsung", ignoreCase = true)) {
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            } else {
                Settings.ACTION_APP_OPEN_BY_DEFAULT_SETTINGS
            }
            Intent(
                action,
                Uri.parse("package:${packageName}")
            ).apply {
                startActivity(this)
            }
        } catch (e: Exception) {
            showToast(e.message ?: "Error")
        }
    }

    fun getCurrentLanguageTag() = LocaleListCompat.getAdjustedDefault()[0]?.toLanguageTag()

    fun Modifier.collapsable(
        state: ScrollableState,
        topBarHeightPx: Float,
        topBarOffsetY: Animatable<Float, AnimationVector1D>,
    ) = composed {
        val scope = rememberCoroutineScope()

        LaunchedEffect(key1 = state.isScrollInProgress) {
            if (!state.isScrollInProgress && topBarOffsetY.value != 0f && topBarOffsetY.value != -topBarHeightPx) {
                val half = topBarHeightPx / 2
                val oldOffsetY = topBarOffsetY.value

                val targetOffsetY = when {
                    abs(topBarOffsetY.value) >= half -> -topBarHeightPx
                    else -> 0f
                }

                launch {
                    state.animateScrollBy(oldOffsetY - targetOffsetY)
                }

                launch {
                    topBarOffsetY.animateTo(targetOffsetY)
                }
            }
        }

        nestedScroll(
            object : NestedScrollConnection {
                override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                    scope.launch {
                        if (state.canScrollForward) {
                            topBarOffsetY.snapTo(
                                targetValue = (topBarOffsetY.value + available.y).coerceIn(
                                    minimumValue = -topBarHeightPx,
                                    maximumValue = 0f,
                                )
                            )
                        }
                    }

                    return Offset.Zero
                }
            }
        )
    }

    fun Modifier.defaultPlaceholder(
        visible: Boolean
    ) = composed {
        placeholder(
            visible = visible,
            color = MaterialTheme.colorScheme.outline,
            highlight = PlaceholderHighlight.fade()
        )
    }
}