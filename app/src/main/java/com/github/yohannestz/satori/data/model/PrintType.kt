package com.github.yohannestz.satori.data.model

enum class PrintType(val value: String) {
    ALL("all"),
    BOOK("book"),
    MAGAZINE("magazine");

    companion object {
        fun valueOfOrNull(value: String) = try {
            PrintType.valueOf(value)
        } catch (e: IllegalArgumentException) {
            null
        }
    }
}