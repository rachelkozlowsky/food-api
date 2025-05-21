package com.fiap.food.config.local

import com.fiap.food.application.domain.User
import com.fiap.food.application.domain.enums.UserRole.ADMIN
import com.fiap.food.config.UserRequestScope
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
@Profile("local")
class LocalUserRequestInterceptor(
    private val userScope: UserRequestScope
): HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {

        userScope.apply {
            this.user = User(
                cpf = "123.456.789-00",
                name = "Admin",
                email = "admin@fiap",
                phone = "1234567890",
                roles = listOf(ADMIN.name)
            )
        }
        MDC.put("user", userScope.user.toString())
        MDC.put("url", request.requestURI)
        return true
    }
}