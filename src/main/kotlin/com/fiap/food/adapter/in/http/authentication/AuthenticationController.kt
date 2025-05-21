package com.fiap.food.adapter.`in`.http.authentication

import com.fiap.food.application.service.authentication.AuthenticationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/auth")
class AuthenticationController(
    private val authenticationService: AuthenticationService
) {
    @PostMapping("/anonymous")
    fun authenticateAnonymous(
        @RequestBody request: AuthenticationRequest.Anonymous
    ): ResponseEntity<AuthenticationResponse> =
        ResponseEntity.ok(authenticationService.authenticate(request))

    @PostMapping("/register")
    fun register(
        @RequestBody request: AuthenticationRequest.Register
    ): ResponseEntity<AuthenticationResponse> =
        ResponseEntity.ok(authenticationService.authenticate(request))

    @PostMapping("/login")
    fun login(
        @RequestBody request: AuthenticationRequest.Login
    ): ResponseEntity<AuthenticationResponse> =
        ResponseEntity.ok(authenticationService.authenticate(request))

    @PostMapping("/refresh")
    fun refreshToken(
        @RequestHeader("Authorization") refreshToken: String
    ): ResponseEntity<String> {
        val token = refreshToken.removePrefix("Bearer ")
        return ResponseEntity.ok(authenticationService.refreshAccessToken(token))
    }
}
