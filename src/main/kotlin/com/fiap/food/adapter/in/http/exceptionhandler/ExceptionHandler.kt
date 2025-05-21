package com.fiap.food.adapter.`in`.http.exceptionhandler

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.fiap.food.application.domain.exceptions.ForbiddenException
import com.fiap.food.application.domain.exceptions.GenericBadRequestException
import com.fiap.food.application.domain.exceptions.GenericPaymentIntegrationException
import com.fiap.food.application.domain.exceptions.InvalidEnumValueException
import com.fiap.food.utils.Loggable
import jakarta.validation.ConstraintViolationException
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class ExceptionHandler: Loggable {

    @ExceptionHandler(GenericBadRequestException::class)
    fun handle(e: GenericBadRequestException): ResponseEntity<ErrorPayload> {
        log(e)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
            .body(ErrorPayload(message = e.message))
    }

    @ExceptionHandler(InvalidEnumValueException::class)
    fun handle(e: InvalidEnumValueException): ResponseEntity<ErrorPayload> {
        log(e)
        return ResponseEntity.status(HttpStatus.NOT_FOUND.value())
            .body(ErrorPayload(message = e.message!!))
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadable(ex: HttpMessageNotReadableException): ResponseEntity<String> {
        val errorMessage = "Invalid value provided for enum: ${ex.message}"
        return ResponseEntity(errorMessage, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleValidationException(ex: ConstraintViolationException): ResponseEntity<String> {
        return ResponseEntity("Validation error: ${ex.message}", HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(NoSuchElementException::class)
    fun handleValidationException(ex: NoSuchElementException): ResponseEntity<String> {
        return ResponseEntity("Validation error: ${ex.message}", HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(GenericPaymentIntegrationException::class)
    fun handle(e: GenericPaymentIntegrationException): ResponseEntity<ErrorPayload> {
        log(e)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
            .body(ErrorPayload(message = e.message))
    }

    @ExceptionHandler(ForbiddenException::class)
    fun handle(e: ForbiddenException): ResponseEntity<ErrorPayload> {
        log(e)
        return ResponseEntity.status(HttpStatus.FORBIDDEN.value())
            .body(ErrorPayload(message = e.message.toString()))
    }



    private fun log(e: Exception){
        logger.error("Handling exception: ${e.javaClass.canonicalName}", e)
    }

}

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class ErrorPayload(
    val message: String,
    val details: String? = null
)