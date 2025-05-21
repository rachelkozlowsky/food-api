package com.fiap.food.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fiap.food.application.domain.User
import com.fiap.food.application.domain.exceptions.BadCredentialsException
import com.fiap.food.application.service.authentication.TokenService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.commons.codec.binary.Base64
import org.json.JSONException
import org.slf4j.MDC
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class AuthenticationFilter(
    private val tokenService: TokenService,
    private val userRequestScope: UserRequestScope
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        val token = authHeader.substring(7)
        if (!tokenService.isTokenValid(token)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token")
            return
        }

        val user = extractUserFromToken(token)
        userRequestScope.user = user

        MDC.put("user", user.toString())
        MDC.put("url", request.requestURI)

        val authorities = user.roles.map { SimpleGrantedAuthority(it) }
        val authentication = UsernamePasswordAuthenticationToken(
            user, null, authorities
        )
        authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
        SecurityContextHolder.getContext().authentication = authentication

        filterChain.doFilter(request, response)
    }

    private fun extractUserFromToken(token: String): User {
        if (!isJWT(token)) {
            throw BadCredentialsException("Invalid token format")
        }
        val body = String(Base64(true).decode(token.split(".")[1]))
        val mapper = ObjectMapper()
        val jsonObject = mapper.readValue(body, MutableMap::class.java)
        val fullName = jsonObject["name"] as? String ?: throw BadCredentialsException("Missing 'name' in token")
        val email = jsonObject["email"] as? String ?: throw BadCredentialsException("Missing 'email' in token")
        val cpf = jsonObject["sub"] as? String ?: throw BadCredentialsException("Missing 'cpf' in token")
        val phone = jsonObject["phone"] as? String ?: throw BadCredentialsException("Missing 'phone' in token")
        val roles = (jsonObject["roles"] as? List<String>) ?: emptyList()
        return User(cpf = cpf, name = fullName, email = email, phone = phone, roles = roles)
    }

    private fun isJWT(jwt: String): Boolean {
        val jwtSplitted = jwt.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (jwtSplitted.size != 3) {
            return false
        }
        try {
            val parts = jwt.split(".")
            Base64(true).decode(parts[0])
            Base64(true).decode(parts[1])
            Base64(true).decode(parts[2])
        } catch (err: JSONException) {
            return false
        }
        return true
    }
}