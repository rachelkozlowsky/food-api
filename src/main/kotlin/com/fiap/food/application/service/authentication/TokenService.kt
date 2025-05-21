package com.fiap.food.application.service.authentication

import com.fiap.food.application.domain.User
import com.fiap.food.application.domain.enums.UserType
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.SecretKey

@Service
class TokenService(
    @Value("\${jwt.key}") private val secret: String = ""
) {

    private val signingKey: SecretKey by lazy {
        val keyBytes = Base64.getDecoder().decode(secret)
        if (keyBytes.size < 32) {
            // Se a chave for muito curta, gera uma chave segura
            Keys.secretKeyFor(SignatureAlgorithm.HS256)
        } else {
            Keys.hmacShaKeyFor(keyBytes)
        }
    }


    fun generateToken(user: User, expirationMs: Long): String {

        try {
            val claims = mapOf(
                "roles" to (user.roles ?: emptyList()),
                "type" to user.type.toString(),
                "name" to user.name,
                "email" to user.email,
                "phone" to user.phone
            )
            val token = Jwts.builder()
                .setClaims(claims)
                .setSubject(user.cpf)
                .setIssuedAt(Date())
                .setExpiration(Date(System.currentTimeMillis() + expirationMs))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact()

            val claimsToken = validateToken(token)

            if (claimsToken != null) {
                println("Token é válido. Claims: $claimsToken")
            } else {
                println("Token é inválido.")
            }

            return token

        } catch (e: Exception) {
            throw IllegalStateException("Error generating token:${e.message}", e)
        }

    }

    fun extractCpf(token: String): String {
        return extractAllClaims(token).subject
    }

    fun extractUserType(token: String): UserType {
        return UserType.valueOf(extractAllClaims(token)["type"] as String)
    }

    fun extractRoles(token: String): List<String> {
        return extractAllClaims(token)["roles"] as List<String>
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(signingKey)
            .build()
            .parseClaimsJws(token)
            .body
    }

    fun validateToken(token: String): Claims? {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .body
        } catch (e: SignatureException) {
            println("Assinatura inválida: ${e.message}")
            null
        } catch (e: Exception) {
            println("Erro ao validar o token: ${e.message}")
            null
        }
    }

    fun isTokenValid(token: String): Boolean {
        return try {
            val claims = validateToken(token)
            claims != null && !claims.expiration.before(Date())
        } catch (e: Exception) {
            false
        }
    }

    fun extractUser(token: String): User {
        val claims = extractAllClaims(token)
        return User(
            cpf = claims.subject,
            name = claims["name"] as String,
            email = claims["email"] as String,
            phone = claims["phone"] as String,
            roles = claims["roles"] as List<String>,
        )
    }
}
