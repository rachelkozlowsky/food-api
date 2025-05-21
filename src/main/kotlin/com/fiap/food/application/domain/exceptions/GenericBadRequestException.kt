package com.fiap.food.application.domain.exceptions

class GenericBadRequestException(override val message: String) : RuntimeException(message)