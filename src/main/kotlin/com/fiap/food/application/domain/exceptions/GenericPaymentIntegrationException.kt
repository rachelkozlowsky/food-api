package com.fiap.food.application.domain.exceptions

class GenericPaymentIntegrationException(override val message: String) : RuntimeException(message)