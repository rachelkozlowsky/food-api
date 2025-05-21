package com.fiap.food.annotation

import com.fiap.food.application.domain.enums.UserRole
import com.fiap.food.application.domain.exceptions.ForbiddenException
import com.fiap.food.application.service.authentication.TokenService
import com.fiap.food.config.UserRequestScope
import com.fiap.food.utils.Loggable
import jakarta.servlet.http.HttpServletRequest
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Roles (vararg val roles: UserRole)

@Aspect
@Component
class RolesAspect(
    private val userRequestScope: UserRequestScope,
    private val env: Environment,
    private val tokenService: TokenService,
    private val request: HttpServletRequest
): Loggable {

    @Before("@annotation(Roles)")
//
//    fun checkRoles(roles: Roles) {
//        val user = userScope.user ?: throw ForbiddenException("User not authenticated")
//        if (!user.roles.containsAll(roles.roles.map { it.name })) {
//            throw ForbiddenException("User does not have the required roles")
//        }
//    }
    fun before(joinPoint: JoinPoint) {
        val methodSignature = joinPoint.signature as MethodSignature
        val method = methodSignature.method
        val roleAnnotation = method.getAnnotation(Roles::class.java)

        val requiredRoles = roleAnnotation.roles.map { it.withSuffix() }
        val user = userRequestScope.user
            ?: throw ForbiddenException("User not found in request scope")
        logger.info("Checking if user=${userRequestScope.user?.cpf} contains any of the roles $requiredRoles")

        if(!userRequestScope.user!!.roles.any { it in requiredRoles }) {
            logger.warn("User=${userRequestScope.user?.cpf} does not have any of the necessary roles $requiredRoles")
            throw ForbiddenException("User has insuficient permission to access this resource")
        } else {
            logger.info("User=${userRequestScope.user?.cpf} authorized")
        }
    }

    @Before("@annotation(Roles)")
    fun checkRoles(roles: Roles) {
        val authHeader = request.getHeader("Authorization")
        if (authHeader.isNullOrBlank() || !authHeader.startsWith("Bearer ")) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing or invalid Authorization header")
        }

        val token = authHeader.substring(7)
        if (!tokenService.isTokenValid(token)) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired token")
        }

        val userRoles = tokenService.extractRoles(token)
        if (!roles.roles.any { it.toString() in userRoles }) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have the required role")
        }
    }

    private fun String.withSuffix(): String {
        return when{
            env.activeProfiles.contains("dev") -> "${this}_DEV"
            env.activeProfiles.contains("hom") -> "${this}_HML"
            else -> this
        }
    }

    private fun UserRole.withSuffix(): String {
        return when{
            env.activeProfiles.contains("dev") -> "${this}_DEV"
            env.activeProfiles.contains("hom") -> "${this}_HML"
            else -> this.toString()
        }
    }
}
