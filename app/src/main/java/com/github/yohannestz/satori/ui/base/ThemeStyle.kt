package com.github.yohannestz.satori.ui.base

import com.github.yohannestz.satori.R


enum class ThemeStyle {
    FOLLOW_SYSTEM, LIGHT, DARK;

    val stringRes
        get() = when (this) {
            FOLLOW_SYSTEM -> R.string.theme_system
            LIGHT -> R.string.theme_light
            DARK -> R.string.theme_dark
        }

    companion object {
        fun valueOfOrNull(value: String) = try {
            valueOf(value)
        } catch (e: IllegalArgumentException) {
            null
        }

        val entriesLocalized = entries.associateWith { it.stringRes }
    }
}