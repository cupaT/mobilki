package com.example.lab3.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface JsonPlaceholderApi {
    @GET("todos")
    suspend fun getTodos(): List<RemoteTodoDto>

    @POST("todos")
    suspend fun createTodo(@Body request: CreateTodoRequest): RemoteTodoDto
}

data class CreateTodoRequest(
    val userId: Int = 1,
    val title: String,
    val completed: Boolean
)
