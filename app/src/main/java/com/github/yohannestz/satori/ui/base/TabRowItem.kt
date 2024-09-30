package com.github.yohannestz.satori.ui.base

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class TabRowItem<T>(
    val value: T,
    @StringRes val title: Int?,
    @DrawableRes val icon: Int? = null,
)
