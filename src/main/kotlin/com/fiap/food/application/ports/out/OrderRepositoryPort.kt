package com.fiap.food.application.ports.out

import com.fiap.food.application.domain.Order
import com.fiap.food.application.domain.enums.OrderStatus

interface OrderRepositoryPort {
    fun save(order: Order): Order
    fun update(order: Order): Order
    fun delete(orderId: String)
    fun get(orderId: String): Order
    fun getByStatus(status: OrderStatus): List<Order>
    fun getAll(): List<Order>
}