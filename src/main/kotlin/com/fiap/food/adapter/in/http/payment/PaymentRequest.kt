package com.fiap.food.adapter.`in`.http.payment

import com.fiap.food.application.domain.Payment
import com.fiap.food.application.domain.enums.PaymentStatus

data class PaymentRequest(

    val transactionAmount: Float,
    val description: String,
    val payerEmail: String = "noreply@email.com",
    val payerFirstName: String = "Name",
    val payerLastName: String = "LastName",
    val payerIdentificationType: String, // CPF ou CNPJ
    val payerIdentificationNumber: String,
    val data: PaymentData?

){
    data class PaymentData(
        val id: String,
        val orderId: String?,
        val status: PaymentStatus = PaymentStatus.PENDING,
        var qrCodeBase64: String,
        var qrCode: String,
        var ticketUrl: String
    )

    fun toDomain() = Payment(
        transactionAmount = this.transactionAmount.toDouble(),
        description = this.description,
        payerEmail = this.payerEmail,
        payerFirstName = this.payerFirstName,
        payerLastName = this.payerLastName,
        payerIdentificationType = this.payerIdentificationType,
        payerIdentificationNumber = this.payerIdentificationNumber,
        data = this.data?.let {
            Payment.PaymentData(
                id = it.id,
                orderId = this.data.orderId,
                status = it.status.toString(),
                qrCodeBase64 = it.qrCodeBase64,
                qrCode = it.qrCode,
                ticketUrl = it.ticketUrl
            )
        }
    )

}