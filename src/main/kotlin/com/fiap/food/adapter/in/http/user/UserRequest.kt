package com.fiap.food.adapter.`in`.http.user

import com.fiap.food.application.domain.User
import com.fiap.food.application.domain.enums.UserRole
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern
import org.hibernate.validator.constraints.br.CPF


data class UserRequest (
    @CPF(message = "Cpf inválido")
    val cpf: String,
    val name: String,
    @Email(message = "Email inválido")
    val email: String,
    @Pattern(regexp = "^\\([0-9]{2}\\) [0-9]{5}-[0-9]{4}\$")
    val phone: String,
    val roles: List<String>?
){
    fun toDomain() = User(
        cpf = this.cpf,
        name = this.name,
        email = this.email,
        phone = this.phone,
        roles = this.roles ?: listOf(UserRole.REGISTERED.name)
    )
}