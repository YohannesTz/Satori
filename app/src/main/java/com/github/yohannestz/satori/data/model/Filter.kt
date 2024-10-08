package com.github.yohannestz.satori.data.model

enum class Filter(val value: String) {
    EBOOKS("ebooks"),
    FREE_EBOOKS("free-ebooks"),
    FULL("full"),
    PAID_EBOOKS("paid-ebooks"),
    PARTIAL("partial"),
    EMPTY("")
}