package com.fiap.food.application.domain

import com.fiap.food.application.domain.enums.OrderStatus
import com.fiap.food.application.domain.enums.PaymentStatus
import java.time.Duration
import java.time.LocalDateTime

data class Order(
    val id: String,
    var status: OrderStatus,
    val user: String?,
    val combo: Combo,
    var total: Double = 0.0,
    val createdAt: LocalDateTime,
    var paymentStatus: PaymentStatus = PaymentStatus.PENDING,
    var updatedAt: LocalDateTime? = null,
    var completedAt: LocalDateTime? = null,
    var statusUpdatedAt: LocalDateTime? = null,
    var statusTimes: MutableMap<OrderStatus, Duration> = mutableMapOf()
) {
    data class Combo(
        val snack: List<Item>? = null,
        val garnish: List<Item>? = null,
        val drink: List<Item>? = null,
        val dessert: List<Item>? = null
    ) {
        data class Item(
            val product: Product,
            val quantity: Int
        )

        // Método para calcular o total de lanches
        fun totalSnacks(): Double {
            return snack?.sumOf { it.product.price * it.quantity } ?: 0.0
        }

        // Método para calcular o total de acompanhamentos
        fun totalGarnishes(): Double {
            return garnish?.sumOf { it.product.price * it.quantity } ?: 0.0
        }

        // Método para calcular o total de bebidas
        fun totalDrinks(): Double {
            return drink?.sumOf { it.product.price * it.quantity } ?: 0.0
        }

        // Método para calcular o total de sobremesas
        fun totalDesserts(): Double {
            return dessert?.sumOf { it.product.price * it.quantity } ?: 0.0
        }

    }

    // Método para atualizar o status
    fun updateStatus(newStatus: OrderStatus) {

        if (status != OrderStatus.DONE) {
            val timeInPreviousStatus = timeInCurrentStatus()
            if (timeInPreviousStatus > Duration.ZERO) {
                statusTimes[status] = (statusTimes[status] ?: Duration.ZERO) + timeInPreviousStatus
            }
        }

        this.status = newStatus
        this.updatedAt = LocalDateTime.now()

        if (newStatus == OrderStatus.DONE) {
            completedAt = LocalDateTime.now()
        }
    }

    // Método para calcular o tempo no status atual
    fun timeInCurrentStatus(): Duration {
        return Duration.between(updatedAt, LocalDateTime.now())
    }

    // Método para calcular o tempo total do pedido
    fun totalTime(): Duration {
        return if (completedAt != null) {
            Duration.between(createdAt, completedAt)
        } else {
            Duration.between(createdAt, LocalDateTime.now())
        }
    }
    //método para calcular o total do pedido
    fun calculateTotal(): Double {
        return combo.totalSnacks() + combo.totalGarnishes() + combo.totalDrinks() + combo.totalDesserts()
    }
}
