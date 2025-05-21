package com.fiap.food.config.local

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fiap.food.utils.Loggable
import jakarta.servlet.*
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component
import java.io.IOException

@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
class AuthenticationFilterByPass(
    val environment: Environment,
    @Value("\${authentication.filter.skip.urls}") private val skipUrls: String,
    private val objectMapper: ObjectMapper
): Filter, Loggable {

    val AUTH_SKIP_URLS = objectMapper.readValue<List<String>>(skipUrls).map{ it.replace("*", "")}

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(req: ServletRequest, res: ServletResponse, chain: FilterChain) {
        val response: HttpServletResponse = res as HttpServletResponse
        val request: HttpServletRequest = req as HttpServletRequest

        response.setHeader("Access-Control-Allow-Origin", "*")
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE")
        response.setHeader("Access-Control-Allow-Headers", "*")

        if ("OPTIONS" == request.method) {
            return
        } else if(
            AUTH_SKIP_URLS.any { request.requestURI.startsWith(prefix = it, ignoreCase = true)} ||
            request.requestURI.contains("swagger") ||
                    request.requestURI.startsWith("swagger")
        ) {
            chain.doFilter(req, res)
        } else {
            chain.doFilter(req, res)
        }
    }

}