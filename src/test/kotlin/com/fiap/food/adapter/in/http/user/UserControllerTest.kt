package com.fiap.food.adapter.`in`.http.user

import com.fiap.food.application.domain.User
import com.fiap.food.application.service.UserService
import org.junit.jupiter.api.Assertions.*
import com.fiap.food.utils.dummyObject
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class UserControllerTest{
    private val userService = mockk<UserService>()
    private val userController = UserController(userService)

    private val response= dummyObject<User>()

    @Test
    fun getAllReturnsOkWithValidRequest() {

        val responseAll = listOf(response)
        every { userService.getAll() } returns responseAll

        val result = userController.getAll()

        assertEquals(responseAll.map { it.toResponse() },result)
        verify { userService.getAll() }
    }

    @Test
    fun getReturnsOkWithValidRequest() {
        val userId = "userId"

        every { userService.get(userId) } returns response

        val result = userController.get(userId)

        assertEquals(response.toResponse(), result)
        verify { userService.get(userId) }
    }

    @Test
    fun createReturnsOkWithValidRequest() {
        val userRequest = mockk<UserRequest>()

        every { userRequest.cpf } returns "111.111.111-11"
        every { userRequest.toDomain() } returns dummyObject<User>().copy(cpf = "111.111.111-11")

        val expected = userRequest.toDomain()
        every { userService.create(expected) } returns expected

        val result = userController.create(userRequest)

        assertEquals(expected.toResponse(), result)
        verify { userService.create(expected) }
    }

    @Test
    fun updateReturnsOkWithValidRequest() {
        val userId = "userId"
        val userRequest = mockk<UserRequest>()

        every { userRequest.cpf } returns "111.111.111-11"
        every { userRequest.toDomain() } returns dummyObject<User>().copy(cpf = "111.111.111-11")

        val expected = userRequest.toDomain()
        every { userService.update(userId, expected) } returns expected

        val result = userController.update(userId, userRequest)

        assertEquals(expected.toResponse(), result)
        verify { userService.update(userId, expected) }
    }

    @Test
    fun deleteReturnsOkWithValidRequest() {
        val userId = "userId"

        every { userService.delete(userId) } returns Unit

        userController.delete(userId)

        verify { userService.delete(userId) }
    }

}