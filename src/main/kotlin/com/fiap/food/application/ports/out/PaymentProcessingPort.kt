package com.fiap.food.application.ports.out

import com.fiap.food.application.domain.Payment

interface PaymentProcessingPort {
    fun createPixPaymentMP(payment: Payment): Payment
}