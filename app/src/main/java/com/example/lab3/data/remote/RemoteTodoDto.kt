package com.example.lab3.data.remote

data class RemoteTodoDto(
    val userId: Int,
    val id: Int,
    val title: String,
    val completed: Boolean
)
