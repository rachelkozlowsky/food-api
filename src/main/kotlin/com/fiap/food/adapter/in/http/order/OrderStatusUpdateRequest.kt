package com.fiap.food.adapter.`in`.http.order

import com.fiap.food.annotation.EnumValidator
import com.fiap.food.application.domain.enums.OrderStatus

data class OrderStatusUpdateRequest(
    @EnumValidator(enumClazz = OrderStatus::class, required = true)
    val newStatus: OrderStatus
)