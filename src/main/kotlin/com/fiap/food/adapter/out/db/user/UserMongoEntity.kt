package com.fiap.food.adapter.out.db.user

import com.fiap.food.application.domain.User
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("Users")
data class UserMongoEntity(
    @Id val cpf: String,
    val name: String,
    val email: String,
    val phone: String,
    val roles: List<String> = emptyList()
)

fun UserMongoEntity.toDomain() = User(
    this.cpf,
    this.name,
    this.email,
    this.phone,
    this.roles
)

fun User.toMongoEntity() = UserMongoEntity(
    this.cpf,
    this.name,
    this.email,
    this.phone,
    this.roles
)