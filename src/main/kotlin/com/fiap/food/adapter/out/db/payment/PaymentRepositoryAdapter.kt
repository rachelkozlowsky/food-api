package com.fiap.food.adapter.out.db.payment

import com.fiap.food.application.domain.Payment
import com.fiap.food.application.ports.out.PaymentRepositoryPort
import com.fiap.food.utils.Loggable
import org.springframework.stereotype.Component

@Component
class PaymentRepositoryAdapter(
    private val repository: PaymentMongoRepository
): PaymentRepositoryPort, Loggable {
    override fun save(payment: Payment): Payment {
        logger.info("Saving payment: $payment")
        return repository.save(payment.toEntity()).toDomain()
    }

    override fun getAllPayments(): List<Payment> {
        logger.info("Fetching all payments")
        return repository.findAll().map { it.toDomain() }
    }

    override fun getPaymentById(paymentId: String): Payment {
        logger.info("Fetching payment with id: $paymentId")
        return repository.findById(paymentId).map { it.toDomain() } .orElseThrow {
            throw RuntimeException("Payment not found")
        }
    }
}