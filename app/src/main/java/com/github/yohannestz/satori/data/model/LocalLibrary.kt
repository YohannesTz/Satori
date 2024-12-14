package com.github.yohannestz.satori.data.model

import android.os.Bundle
import androidx.navigation.NavType
import com.github.yohannestz.satori.R

enum class LocalLibrary(val value: String, val label: Int) {
    BOOKMARKS("bookmarks", R.string.book_marks),
    DOWNLOADS("downloads", R.string.downloads);

    companion object {
        val navType = object : NavType<LocalLibrary>(isNullableAllowed = false) {
            override fun get(bundle: Bundle, key: String): LocalLibrary? {
                return try {
                    bundle.getString(key)?.let {
                        LocalLibrary.valueOf(it)
                    }
                } catch (ex: Exception) {
                    null
                }
            }

            override fun parseValue(value: String): LocalLibrary {
                return try {
                    LocalLibrary.valueOf(value)
                } catch (ex: Exception) {
                    BOOKMARKS
                }
            }

            override fun put(bundle: Bundle, key: String, value: LocalLibrary) {
                bundle.putString(key, value.name)
            }
        }
    }

    override fun toString(): String {
        return name
    }
}