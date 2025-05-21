package com.fiap.food.adapter.`in`.http.authentication

data class AuthenticationResponse(
    val token: String,
    val refreshToken: String,
    val userId: String,
    val type: String = "Bearer"
)
