package com.fiap.food.application.service

import com.fiap.food.adapter.out.db.payment.PaymentRepositoryAdapter
import com.fiap.food.application.domain.Payment
import com.fiap.food.application.domain.enums.PaymentStatus
import com.fiap.food.application.ports.out.PaymentProcessingPort
import com.fiap.food.utils.Loggable
import org.springframework.stereotype.Service

@Service
class PaymentService(
    private val processingRepository: PaymentProcessingPort,
    private val repository: PaymentRepositoryAdapter,
    private val orderService: OrderService,
): Loggable {

    fun createPixPaymentMP(payment: Payment): Payment {
        logger.info("Creating payment: $payment")
        return processingRepository.createPixPaymentMP(payment)
    }

    fun getAllPayments(): List<Payment> {
        logger.info("Getting all payments")
        return repository.getAllPayments()
    }

    fun getPaymentById(paymentId: String): Payment {
        logger.info("Getting payment with ID: $paymentId")
        return repository.getPaymentById(paymentId)
    }

    fun updatePaymentStatus(paymentId: String, status: PaymentStatus): Payment {
        logger.info("Updating payment status for ID: $paymentId to status: $status")
        val payment = getPaymentById(paymentId)
        payment.data?.status = status.toString()
        val order = orderService.get(payment.data?.orderId!!)
        order.paymentStatus = status
        orderService.update(order.id, order)

        return repository.save(payment)
    }
}