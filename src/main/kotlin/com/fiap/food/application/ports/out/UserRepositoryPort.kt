package com.fiap.food.application.ports.out

import com.fiap.food.application.domain.User

interface UserRepositoryPort {
    fun save(user: User): User
    fun update(user: User): User
    fun delete(userId: String)
    fun get(userId: String): User?
    fun getAll(): List<User>
}