package com.github.yohannestz.satori.data.model

import com.github.yohannestz.satori.R

enum class ViewMode(val label: Int) {
    GRID(R.string.grid),
    LIST(R.string.list);

    companion object {
        fun valueOfOrNull(value: String) = try {
            valueOf(value)
        } catch (e: IllegalArgumentException) {
            null
        }
    }
}