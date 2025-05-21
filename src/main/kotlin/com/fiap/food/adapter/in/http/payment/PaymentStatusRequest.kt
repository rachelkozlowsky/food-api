package com.fiap.food.adapter.`in`.http.payment

import com.fiap.food.application.domain.enums.PaymentStatus

data class PaymentStatusRequest(
    val status: PaymentStatus,
)