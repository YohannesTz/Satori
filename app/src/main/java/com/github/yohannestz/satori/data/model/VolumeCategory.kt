package com.github.yohannestz.satori.data.model

import com.github.yohannestz.satori.R

enum class VolumeCategory(val value: String, val lable: Int) {
    ROMANCE("romance", R.string.romance),
    HISTORY("history", R.string.history),
    BIOGRAPHY("biography", R.string.biography),
    AUTOBIOGRAPHY("autobiography", R.string.autobiography),
    FICTION("fiction", R.string.fiction),
    BUSINESS_INVESTING("business and investing", R.string.business_investing),
    SCIENCE_MATH("science and math", R.string.science_math),
    COMPUTER_TECHNOLOGY("computer and technology", R.string.computer_technology),
    SELF_HELP("self-help", R.string.self_help)
}