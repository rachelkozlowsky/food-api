package com.fiap.food.application.service.authentication

import com.fiap.food.adapter.`in`.http.authentication.AuthenticationRequest
import com.fiap.food.adapter.`in`.http.authentication.AuthenticationResponse
import com.fiap.food.application.domain.User
import com.fiap.food.application.domain.enums.UserRole.ANONYMOUS
import com.fiap.food.application.domain.enums.UserRole.REGISTERED
import com.fiap.food.application.domain.exceptions.BadCredentialsException
import com.fiap.food.application.service.UserService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthenticationService(
    private val userService: UserService,
    private val tokenService: TokenService,
    private val refreshTokenRepository: RefreshTokenRepository,
    @Value("\${jwt.accessTokenExpiration}") private val accessTokenExpiration: Long = 0,
    @Value("\${jwt.refreshTokenExpiration}") private val refreshTokenExpiration: Long = 0
) {
    fun authenticate(request: AuthenticationRequest): AuthenticationResponse {
        val user = when(request) {
            is AuthenticationRequest.Anonymous -> createAnonymousUser()
            is AuthenticationRequest.Login -> authenticateRegisteredUser(request)
            is AuthenticationRequest.Register -> registerUser(request)
            else -> {
                throw IllegalArgumentException("Invalid authentication request")
            }
        }

        val accessToken = createAccessToken(user)
        val refreshToken = createRefreshToken(user)
        refreshTokenRepository.save(refreshToken, user)

        return AuthenticationResponse(
            token = accessToken,
            refreshToken = refreshToken,
            userId = user.cpf
        )
    }

    private fun createAnonymousUser(): User {
        // Gera um CPF temporário para usuários anônimos
        val tempCpf = "ANON${UUID.randomUUID().toString().take(11)}"
        return User(
            cpf = tempCpf,
            name = tempCpf,
            email = "${tempCpf}@anonymous.local",
            phone = tempCpf,
            roles = listOf(ANONYMOUS.name)
        )
    }

    private fun authenticateRegisteredUser(request: AuthenticationRequest.Login): User {
        val user = userService.get(request.cpf)
            ?: throw BadCredentialsException("User not found. Please register first.")

        if (user.phone != request.phone) {
            throw BadCredentialsException("Invalid credentials")
        }

        return user
    }

    private fun registerUser(request: AuthenticationRequest.Register): User {
        userService.get(request.cpf)?.let {
            throw IllegalArgumentException("User with this CPF already exists")
        }

        return User(
            cpf = request.cpf,
            name = request.name,
            email = request.email,
            phone = request.phone,
            roles = listOf(REGISTERED.name)
        ).also { userService.create(it) }
    }

    fun refreshAccessToken(refreshToken: String): String {
        val cpf = tokenService.extractCpf(refreshToken)
        return cpf.let { userCpf ->
            val currentUser = userService.get(userCpf)
            val refreshTokenUser = refreshTokenRepository.findUserDetailsByToken(refreshToken)

            if (currentUser?.cpf == refreshTokenUser?.cpf)
                createAccessToken(currentUser!!)
            else
                throw IllegalArgumentException("Invalid refresh token")
        }
    }

    private fun createAccessToken(user: User): String =
        tokenService.generateToken(user, accessTokenExpiration)

    private fun createRefreshToken(user: User): String =
        tokenService.generateToken(user, refreshTokenExpiration)
}