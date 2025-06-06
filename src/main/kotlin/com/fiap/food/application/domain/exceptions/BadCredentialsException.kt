package com.fiap.food.application.domain.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class BadCredentialsException(override val message: String) : RuntimeException(message)