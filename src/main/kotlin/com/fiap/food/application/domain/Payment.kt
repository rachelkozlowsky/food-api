package com.fiap.food.application.domain

import com.fiap.food.application.domain.enums.PaymentStatus

data class Payment(

    val transactionAmount: Double,
    val description: String,
    val payerEmail: String,
    val payerFirstName: String,
    val payerLastName: String,
    val payerIdentificationType: String, // CPF ou CNPJ
    val payerIdentificationNumber: String,
    val data: PaymentData?
){
    data class PaymentData(
        val id: String,
        val orderId: String?,
        var status: String = PaymentStatus.PENDING.name,
        var qrCodeBase64: String,
        var qrCode: String,
        var ticketUrl: String
    )
}
