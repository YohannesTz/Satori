package com.github.yohannestz.satori.data.model

import android.os.Bundle
import androidx.navigation.NavType
import com.github.yohannestz.satori.R

enum class VolumeCategory(val value: String, val label: Int) {
    BUSINESS_ECONOMICS("business and economics", R.string.business_economics),
    HISTORY("history", R.string.history),
    BIOGRAPHY("biography", R.string.biography),
    PHILOSOPHY("philosophy", R.string.philosophy),
    SCIENCE_MATH("science and math", R.string.science_math),
    ROMANCE("romance", R.string.romance),
    FICTION("fiction", R.string.fiction),
    AUTOBIOGRAPHY("autobiography", R.string.autobiography),
    COMPUTER_TECHNOLOGY("computer and technology", R.string.computer_technology),
    SELF_HELP("self-help", R.string.self_help);

    companion object {
        val navType = object : NavType<VolumeCategory>(isNullableAllowed = false) {
            override fun get(bundle: Bundle, key: String): VolumeCategory? {
                return try {
                    bundle.getString(key)?.let {
                        VolumeCategory.valueOf(it)
                    }
                } catch (_: IllegalStateException) {
                    null
                }
            }

            override fun parseValue(value: String): VolumeCategory {
                return try {
                    VolumeCategory.valueOf(value)
                } catch (_: IllegalStateException) {
                    BUSINESS_ECONOMICS
                }
            }

            override fun put(bundle: Bundle, key: String, value: VolumeCategory) {
                bundle.putString(key, value.name)
            }
        }
    }

    override fun toString(): String {
        return name
    }
}