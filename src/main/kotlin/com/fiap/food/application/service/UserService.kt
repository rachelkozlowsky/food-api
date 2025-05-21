package com.fiap.food.application.service

import com.fiap.food.application.domain.User
import com.fiap.food.application.domain.exceptions.GenericBadRequestException
import com.fiap.food.application.ports.out.UserRepositoryPort
import com.fiap.food.utils.Loggable
import org.springframework.stereotype.Service

@Service
class UserService(
    private val repository: UserRepositoryPort
): Loggable {
    fun create(user: User): User {
        logger.info("Creating user: $user")
        val existingUser = repository.get(user.cpf)
        if (existingUser != null) {
            throw GenericBadRequestException("User with CPF ${user.cpf} already exists. Try updating instead.")
        }
        return repository.save(user)
    }

    fun get(cpf: String): User? {
        logger.info("Getting user with CPF: $cpf")
        return repository.get(cpf)
    }

    fun update(cpf: String, user: User): User {
        logger.info("Updating user with CPF: $cpf")
        val existingUser = repository.get(cpf)
            ?: throw GenericBadRequestException("User with CPF $cpf does not exist. Try creating instead.")
        val userToUpdate = existingUser.copy(
            name = user.name,
            email = user.email,
            phone = user.phone,
            roles = user.roles,
        )
        return repository.update(userToUpdate)
    }

    fun delete(cpf: String) {
        logger.info("Deleting user with CPF: $cpf")
        val existingUser = repository.get(cpf)
            ?: throw GenericBadRequestException("User with CPF $cpf does not exist. Try creating instead.")
        repository.delete(existingUser.cpf)
    }

    fun getAll(): List<User> {
        logger.info("Getting all users")
        return repository.getAll()
    }
}