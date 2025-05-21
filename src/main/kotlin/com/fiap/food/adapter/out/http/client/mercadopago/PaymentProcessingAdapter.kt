package com.fiap.food.adapter.out.http.client.mercadopago

import com.fiap.food.adapter.out.db.payment.PaymentMongoEntity
import com.fiap.food.adapter.out.db.payment.PaymentMongoRepository
import com.fiap.food.application.domain.Payment
import com.fiap.food.application.domain.enums.OrderStatus
import com.fiap.food.application.domain.enums.PaymentStatus
import com.fiap.food.application.domain.exceptions.GenericPaymentIntegrationException
import com.fiap.food.application.ports.out.PaymentProcessingPort
import com.fiap.food.application.service.OrderService
import com.fiap.food.utils.Loggable
import com.mercadopago.client.common.IdentificationRequest
import com.mercadopago.client.payment.PaymentClient
import com.mercadopago.client.payment.PaymentCreateRequest
import com.mercadopago.client.payment.PaymentPayerRequest
import com.mercadopago.exceptions.MPApiException
import com.mercadopago.exceptions.MPException
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset


@Component
class PaymentAdapter(
    private val paymentRepository: PaymentMongoRepository,

    private val orderService: OrderService
): PaymentProcessingPort, Loggable {

    override fun createPixPaymentMP(payment: Payment): Payment = createPixPayment(payment)

    fun createPixPayment(request: Payment): Payment {
        logger.info("Criando pagamento PIX com os dados: $request")
        try {
            // Cria o cliente de pagamento
            val client = PaymentClient()

            // Configura os dados de identificação do pagador
            val identification = IdentificationRequest.builder()
                .type(request.payerIdentificationType)
                .number(request.payerIdentificationNumber)
                .build()

            // Configura os dados do pagador
            val payer = PaymentPayerRequest.builder()
                .email(request.payerEmail)
                .firstName(request.payerFirstName)
                .lastName(request.payerLastName)
                .identification(identification)
                .build()

            // Cria o pedido de pagamento
            val paymentRequest = PaymentCreateRequest.builder()
                .transactionAmount(BigDecimal.valueOf(request.transactionAmount.toDouble()))
                .description(request.description)
                .paymentMethodId("pix")
                .payer(payer)
                .dateOfExpiration(OffsetDateTime.now().plusHours(24).withOffsetSameInstant(ZoneOffset.of("-03:00")))
                .build()

            // Executa o pedido e obtém o pagamento
            val payment = client.create(paymentRequest)

            logger.info("Pagamento PIX criado com sucesso: $payment")
            // salva o pagamento no banco de dados //Atualiza o status do pagamento
            paymentRepository.save(mapToEntity(mapToDomain(payment = payment, request = request)))

            //atualiza status do pedido
            orderService.updateOrderStatus(request.data?.orderId.toString(), OrderStatus.RECEIVED)

            // Mapeia para o objeto de resposta
            return mapToResponse(payment = payment, request = request)
        } catch (e: MPApiException) {
            logger.error("Erro ao criar pagamento PIX: ${e.message}", e)
            throw GenericPaymentIntegrationException("Erro ao criar pagamento PIX: " + e.message)
        } catch (e: MPException) {
            logger.error("Erro ao criar pagamento PIX: ${e.message}", e)
            throw GenericPaymentIntegrationException("Erro ao criar pagamento PIX: " + e.message)
        }
    }

    private fun mapToResponse(payment: com.mercadopago.resources.payment.Payment, request: Payment): Payment {
        val response = Payment(
            transactionAmount = payment.transactionAmount.toDouble(),
            description = payment.description,
            payerEmail = payment.payer.email ?: request.payerEmail,
            payerFirstName = payment.payer.firstName ?: request.payerFirstName,
            payerLastName = payment.payer.lastName ?: request.payerLastName,
            payerIdentificationType = payment.payer.identification.type ?: request.payerIdentificationType,
            payerIdentificationNumber = payment.payer.identification.number ?: request.payerIdentificationNumber,
            data = Payment.PaymentData(
                id = payment.id.toString(),
                orderId = request.data?.orderId,
                status = PaymentStatus.PENDING.toString(),
                qrCodeBase64 = payment.pointOfInteraction?.transactionData?.qrCodeBase64 ?: "",
                qrCode = payment.pointOfInteraction?.transactionData?.qrCode ?: "",
                ticketUrl = payment.pointOfInteraction?.transactionData?.ticketUrl ?: "",
            )
        )

        if (payment.pointOfInteraction != null &&
            payment.pointOfInteraction.transactionData != null
        ) {
            response.data?.qrCodeBase64 = payment.pointOfInteraction.transactionData.qrCodeBase64
            response.data?.qrCode = payment.pointOfInteraction.transactionData.qrCode
            response.data?.ticketUrl = payment.pointOfInteraction.transactionData.ticketUrl
        }
        return response
    }

    private fun mapToDomain(payment: com.mercadopago.resources.payment.Payment, request: Payment): Payment {
        return Payment(
            transactionAmount = payment.transactionAmount.toDouble(),
            description = payment.description,
            payerEmail = payment.payer.email ?: request.payerEmail,
            payerFirstName = payment.payer.firstName ?: request.payerFirstName,
            payerLastName = payment.payer.lastName ?: request.payerLastName,
            payerIdentificationType = payment.payer.identification.type ?: request.payerIdentificationType,
            payerIdentificationNumber = payment.payer.identification.number ?: request.payerIdentificationNumber,
            data = Payment.PaymentData(
                id = payment.id.toString(),
                orderId = request.data?.orderId,
                status = PaymentStatus.PENDING.toString(),
                qrCodeBase64 = payment.pointOfInteraction?.transactionData?.qrCodeBase64 ?: "",
                qrCode = payment.pointOfInteraction?.transactionData?.qrCode ?: "",
                ticketUrl = payment.pointOfInteraction?.transactionData?.ticketUrl ?: "",
            )
        )
    }

    private fun mapToEntity(payment: Payment): PaymentMongoEntity {
        return PaymentMongoEntity(
            id = payment.data?.id,
            orderId = payment.data?.orderId,
            status = payment.data?.status,
            qrCodeBase64 = payment.data?.qrCodeBase64 ?: "",
            qrCode = payment.data?.qrCode ?: "",
            ticketUrl = payment.data?.ticketUrl ?: "",
            transactionAmount = payment.transactionAmount,
            description = payment.description,
            payerEmail = payment.payerEmail,
            payerFirstName = payment.payerFirstName,
            payerLastName = payment.payerLastName,
            payerIdentificationType = payment.payerIdentificationType,
            payerIdentificationNumber = payment.payerIdentificationNumber,
            createdAt = LocalDateTime.now().toString(),
        )
    }

}