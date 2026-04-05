package com.example.bookbrowser.data.network

import com.example.bookbrowser.network.BuildConfig
import com.example.bookbrowser.data.model.BookItem
import com.example.bookbrowser.data.model.BookResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BookApi {
    @GET("volumes")
    suspend fun getBooks(
        @Query("q") query: String = "subject:fiction"
    ): BookResponse

    @GET("volumes/{bookId}")
    suspend fun getBookDetails(
        @Path("bookId") bookId: String
    ): BookItem
}

object NetworkModule {
    private val okHttpClient: OkHttpClient by lazy {
        provideNetworkClient()
    }

    val api: BookApi by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BookApi::class.java)
    }
}
