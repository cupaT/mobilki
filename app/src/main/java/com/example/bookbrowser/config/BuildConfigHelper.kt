package com.example.bookbrowser.config

import com.example.bookbrowser.BuildConfig

object BuildConfigHelper {
    val apiBaseUrl: String = BuildConfig.API_BASE_URL
    val defaultBookQuery: String = BuildConfig.DEFAULT_BOOK_QUERY
    val isDebug: Boolean = BuildConfig.DEBUG
    val isPremium: Boolean = BuildConfig.IS_PREMIUM
    val buildLabel: String = BuildConfig.BUILD_LABEL
    val flavorName: String = BuildConfig.FLAVOR
}
