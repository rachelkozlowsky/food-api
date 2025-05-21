package com.fiap.food.application.ports.out

import com.fiap.food.application.domain.Payment

interface PaymentRepositoryPort {
    fun save(payment: Payment): Payment
    fun getAllPayments(): List<Payment>
    fun getPaymentById(paymentId: String): Payment
}