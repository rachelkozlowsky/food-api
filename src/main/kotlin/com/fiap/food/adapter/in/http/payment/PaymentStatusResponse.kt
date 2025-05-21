package com.fiap.food.adapter.`in`.http.payment

import com.fiap.food.application.domain.Payment

data class PaymentStatusResponse(
    val status: String,
)

fun Payment.toStatusResponse() = PaymentStatusResponse(
    status = (this.data?.status.toString())
)