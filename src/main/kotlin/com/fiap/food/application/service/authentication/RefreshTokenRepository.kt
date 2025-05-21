package com.fiap.food.application.service.authentication

import com.fiap.food.application.domain.User
import org.springframework.stereotype.Repository

@Repository
class RefreshTokenRepository {
    private val tokens = mutableMapOf<String, User>()

    fun findUserDetailsByToken(token: String): User? =
        tokens[token]

    fun save(token: String, userDetails: User) {
        tokens[token] = userDetails
    }
}