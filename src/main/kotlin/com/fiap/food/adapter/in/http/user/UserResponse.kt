package com.fiap.food.adapter.`in`.http.user

import com.fiap.food.application.domain.User

data class UserResponse(
    val cpf: String,
    val name: String?,
    val email: String?,
    val phone: String?,
    val roles: List<String>
)

fun User.toResponse() = UserResponse(
    cpf = this.cpf,
    name = this.name,
    email = this.email ,
    phone = this.phone,
    roles = this.roles
)
