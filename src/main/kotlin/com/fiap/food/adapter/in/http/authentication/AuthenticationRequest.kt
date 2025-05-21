package com.fiap.food.adapter.`in`.http.authentication

sealed class AuthenticationRequest {
    data class Anonymous(val deviceId: String? = null) : AuthenticationRequest()
    
    data class Login(
        val cpf: String,
        val phone: String
    ) : AuthenticationRequest()
    
    data class Register(
        val name: String,
        val email: String,
        val phone: String,
        val cpf: String
    ) : AuthenticationRequest()

    data class Refresh(
        val refreshToken: String
    ) : AuthenticationRequest()
}
