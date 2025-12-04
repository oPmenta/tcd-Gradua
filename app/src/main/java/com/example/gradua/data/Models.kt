package com.example.gradua.data

data class Question(
    val id: Int,
    val title: String,
    val subject: String,
    val year: String,
    val text: String
)

data class UserProfile(
    val name: String,
    val email: String,
    val phone: String
)