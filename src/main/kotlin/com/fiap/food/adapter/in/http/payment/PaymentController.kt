package com.fiap.food.adapter.`in`.http.payment

import com.fiap.food.application.service.PaymentService
import com.fiap.food.utils.Loggable
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/payments")
@CrossOrigin(origins = ["*"])
class PaymentController(
    private val paymentService: PaymentService
): Loggable {

    @PostMapping("/pix")
    fun createPixPayment(
        @RequestBody paymentRequest: PaymentRequest
    ): PaymentResponse {
        logger.info("Create payment with user: ${paymentRequest.payerEmail}")
         val payment = paymentService.createPixPaymentMP(paymentRequest.toDomain())
        return PaymentResponse(
            id = (payment.data?.id ?: 0).toString(),
            status = (payment.data?.status ?: "").toString(),
            qrCodeBase64 = payment.data?.qrCodeBase64 ?: "",
            qrCode = payment.data?.qrCode ?: "",
            ticketUrl = payment.data?.ticketUrl ?: ""
        )
    }

    @GetMapping
    fun getAllPayments(): List<PaymentResponse> {
        logger.info("Get all payments")
        return paymentService.getAllPayments().map {
            PaymentResponse(
                id = it.data?.id.toString(),
                status = (it.data?.status ?: "").toString(),
                qrCodeBase64 = it.data?.qrCodeBase64 ?: "",
                qrCode = it.data?.qrCode ?: "",
                ticketUrl = it.data?.ticketUrl ?: ""
            )
        }
    }

    @GetMapping("/{paymentId}")
    fun getPaymentById(@PathVariable paymentId: String): PaymentResponse {
        logger.info("Get payment with id: $paymentId")
        return paymentService.getPaymentById(paymentId)
            .let {
                PaymentResponse(
                    id = it.data?.id.toString(),
                    status = (it.data?.status ?: "").toString(),
                    qrCodeBase64 = it.data?.qrCodeBase64 ?: "",
                    qrCode = it.data?.qrCode ?: "",
                    ticketUrl = it.data?.ticketUrl ?: ""
                )
            }
    }


    @PostMapping("/{paymentId}") //fake checkout
    fun updatePayment(
        @PathVariable paymentId: String,
        @RequestBody paymentRequest: PaymentStatusRequest
    ): PaymentStatusResponse {
        logger.info("Update payment with id: $paymentId")
        return paymentService.updatePaymentStatus(paymentId, paymentRequest.status).toStatusResponse()
    }


}

