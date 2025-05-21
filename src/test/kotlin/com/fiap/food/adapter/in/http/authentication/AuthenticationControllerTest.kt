package com.fiap.food.adapter.`in`.http.authentication

import com.fiap.food.application.service.authentication.AuthenticationService
import com.fiap.food.utils.dummyObject
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

class AuthenticationControllerTest {

    private val authenticationService = mockk<AuthenticationService>()
    private val authenticationController = AuthenticationController(authenticationService)

    private val response = dummyObject<AuthenticationResponse>()

    @Test
    fun authenticateAnonymousReturnsOkWithValidRequest() {
        val request = AuthenticationRequest.Anonymous("deviceId")

        every { authenticationService.authenticate(request)} returns response

        val result = authenticationController.authenticateAnonymous(request)

        assertEquals(HttpStatus.OK, result.statusCode)
        assertEquals(response, result.body)

        verify { authenticationService.authenticate(request) }

    }

    @Test
    fun registerReturnsOkWithValidRequest() {
        val request = AuthenticationRequest.Register("username", "email@email.com", "21-212121211", "111.111.111-11")

        every { authenticationService.authenticate(request)} returns response

        val result = authenticationController.register(request)

        assertEquals(HttpStatus.OK, result.statusCode)
        assertEquals(response, result.body)
        verify { authenticationService.authenticate(request) }
    }

    @Test
    fun loginReturnsOkWithValidRequest() {
        val request = AuthenticationRequest.Login("username", "phone")

        every {authenticationService.authenticate(request)} returns response

        val result = authenticationController.login(request)

        assertEquals(HttpStatus.OK, result.statusCode)
        assertEquals(response, result.body)
        verify { authenticationService.authenticate(request) }
    }

    @Test
    fun refreshTokenReturnsOkWithValidToken() {
        val refreshToken = "Bearer validRefreshToken"
        val newToken = "newAccessToken"
        every { authenticationService.refreshAccessToken("validRefreshToken")} returns newToken

        val result = authenticationController.refreshToken(refreshToken)

        assertEquals(HttpStatus.OK, result.statusCode)
        assertEquals(newToken, result.body)
        verify { authenticationService.refreshAccessToken("validRefreshToken") }
    }
}