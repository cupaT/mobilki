package com.example.bookbrowser.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

internal fun provideNetworkClient(): OkHttpClient {
    val logger = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    return OkHttpClient.Builder()
        .addInterceptor(logger)
        .build()
}
