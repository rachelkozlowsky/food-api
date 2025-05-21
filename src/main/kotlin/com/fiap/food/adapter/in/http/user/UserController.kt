package com.fiap.food.adapter.`in`.http.user

import com.fiap.food.annotation.Roles
import com.fiap.food.application.domain.enums.UserRole.ADMIN
import com.fiap.food.application.service.UserService
import com.fiap.food.utils.Loggable
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/users")
@CrossOrigin(origins = ["*"])
class UserController(
    val userService: UserService
): Loggable {

    @GetMapping("/{userId}")
    fun get(@PathVariable userId: String): UserResponse? {
        logger.info("Get user with cpf: $userId")
        return userService.get(userId)?.toResponse()
    }

    @GetMapping
    fun getAll(): List<UserResponse> {
        logger.info("Get all users")
        return userService.getAll().map { it.toResponse() }
    }

    @PostMapping
    fun create(@Valid @RequestBody userRequest: UserRequest): UserResponse {
        logger.info("Create user with cpf: ${userRequest.cpf}")
        return userService.create(userRequest.toDomain()).toResponse()
    }

    @PutMapping("/{userId}")
    fun update(@PathVariable userId: String, @Valid @RequestBody userRequest: UserRequest): UserResponse {
        logger.info("Update user with cpf: $userId")
        return userService.update(userId, userRequest.toDomain()).toResponse()
    }

    @Roles(ADMIN)
    @DeleteMapping("/{userId}")
    fun delete(@PathVariable userId: String) {
        logger.info("Delete user with cpf: $userId")
        userService.delete(userId)
    }
}

