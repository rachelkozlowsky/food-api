package com.fiap.food.adapter.out.db.payment

import com.fiap.food.application.domain.Payment
import com.fiap.food.utils.toBrazilianTime
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("Payments")
data class PaymentMongoEntity(
    @Id val id: String?,
    val orderId: String?,
    val status: String?,
    var qrCodeBase64: String,
    var qrCode: String,
    var ticketUrl: String,
    val transactionAmount: Double?,
    val description: String?,
    val payerEmail: String?,
    val payerFirstName: String,
    val payerLastName: String,
    val payerIdentificationType: String,
    val payerIdentificationNumber: String,
    val createdAt: String?,
) {

    fun toDomain(): Payment {
        return Payment(
            transactionAmount = this.transactionAmount ?: 0.0,
            description = this.description ?: "",
            payerEmail = this.payerEmail ?: "",
            payerFirstName = this.payerFirstName,
            payerLastName = this.payerLastName,
            payerIdentificationType = this.payerIdentificationType,
            payerIdentificationNumber = this.payerIdentificationNumber,
            data = Payment.PaymentData(
                id = this.id ?: "",
                orderId = this.orderId,
                status = this.status ?: "",
                qrCodeBase64 = this.qrCodeBase64,
                qrCode = this.qrCode,
                ticketUrl = this.ticketUrl
            )
        )
    }
}

fun Payment.toEntity(): PaymentMongoEntity {
    return PaymentMongoEntity(
        id = this.data?.id,
        orderId = this.data?.orderId,
        status = this.data?.status,
        qrCodeBase64 = this.data?.qrCodeBase64 ?: "",
        qrCode = this.data?.qrCode ?: "",
        ticketUrl = this.data?.ticketUrl ?: "",
        transactionAmount = this.transactionAmount,
        description = this.description,
        payerEmail = this.payerEmail,
        payerFirstName = this.payerFirstName,
        payerLastName = this.payerLastName,
        payerIdentificationType = this.payerIdentificationType,
        payerIdentificationNumber = this.payerIdentificationNumber,
        createdAt = LocalDateTime.now().toBrazilianTime()
    )
}