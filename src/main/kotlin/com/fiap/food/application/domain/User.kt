package com.fiap.food.application.domain

import com.fiap.food.application.domain.enums.UserRole.ANONYMOUS
import com.fiap.food.application.domain.enums.UserType
import java.util.*

data class User(
    val cpf: String,
    val name: String,
    val email: String,
    val phone: String,
    val roles: List<String>
) {

    companion object {
        private const val ANONYMOUS_PREFIX = "ANON_"

        fun createAnonymous(deviceId: String? = null): User {
            val anonymousId = deviceId ?: UUID.randomUUID().toString().take(8)
            return User(
                cpf = "${ANONYMOUS_PREFIX}CPF_$anonymousId",
                name = "${ANONYMOUS_PREFIX}USER_$anonymousId",
                email = "$anonymousId@anonymous.local",
                phone = "${ANONYMOUS_PREFIX}PHONE_$anonymousId",
                roles = listOf(ANONYMOUS.name)
            )
        }
    }

    val type: UserType
        get() = if (isAnonymous()) UserType.ANONYMOUS else UserType.IDENTIFIED

    private fun isAnonymous(): Boolean = cpf.startsWith(ANONYMOUS_PREFIX)

    fun validate() {
        require(cpf.isNotBlank()) { "CPF is required" }
        require(name.isNotBlank()) { "Name is required" }
        require(email.isNotBlank()) { "Email is required" }
        require(phone.isNotBlank()) { "Phone is required" }
        require(roles.isNotEmpty()) { "At least one role is required" }

        if (!isAnonymous()) {
            require(!cpf.startsWith(ANONYMOUS_PREFIX)) { "Invalid CPF format for registered user" }
            require(!name.startsWith(ANONYMOUS_PREFIX)) { "Invalid name format for registered user" }
            require(!email.endsWith("@anonymous.local")) { "Invalid email format for registered user" }
            require(!phone.startsWith(ANONYMOUS_PREFIX)) { "Invalid phone format for registered user" }
            require(ANONYMOUS.name !in roles) { "Invalid role for registered user" }
        }
    }
}