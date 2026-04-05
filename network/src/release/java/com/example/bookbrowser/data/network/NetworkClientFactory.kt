package com.example.bookbrowser.data.network

import okhttp3.OkHttpClient

internal fun provideNetworkClient(): OkHttpClient = OkHttpClient.Builder().build()
