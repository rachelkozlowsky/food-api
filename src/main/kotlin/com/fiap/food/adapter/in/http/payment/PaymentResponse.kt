package com.fiap.food.adapter.`in`.http.payment

data class PaymentResponse(

    val id: String,
    val status: String,
    var qrCodeBase64: String,
    var qrCode: String,
    var ticketUrl: String
)

