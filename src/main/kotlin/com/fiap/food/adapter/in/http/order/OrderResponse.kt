package com.fiap.food.adapter.`in`.http.order

import com.fiap.food.application.domain.Order
import com.fiap.food.application.domain.enums.OrderStatus
import com.fiap.food.utils.formatDuration
import com.fiap.food.utils.toBrazilianTime

data class OrderResponse(
    val id: String,
    val status: OrderStatus,
    val user: String?,
    val combo: ComboResponse,
    val createdAt: String,
    val updatedAt: String? = null,
    val completedAt: String? = null,
    val paymentStatus: String? = null,
    val totals: OrderTotalsResponse,
    val totalTime: String,
    val statusTimes: Map<OrderStatus, String>
    ) {
    data class ComboResponse(
        val snack: List<ItemResponse>? = null,
        val garnish: List<ItemResponse>? = null,
        val drink: List<ItemResponse>? = null,
        val dessert: List<ItemResponse>? = null
    ) {
        data class ItemResponse(
            val productId: String,
            val quantity: Int
        )
    }

    data class OrderTotalsResponse(
        val totalSnacks: Double,
        val totalGarnishes: Double,
        val totalDrinks: Double,
        val totalDesserts: Double,
        val totalOrder: Double
    )
}
fun Order.toResponse() = OrderResponse(
    id = this.id,
    status = this.status,
    user = this.user ?: "Annonymous",
    combo = OrderResponse.ComboResponse(
        snack = this.combo.snack?.map { OrderResponse.ComboResponse.ItemResponse(it.product.id, it.quantity) } ?: emptyList(),
        garnish = this.combo.garnish?.map { OrderResponse.ComboResponse.ItemResponse(it.product.id, it.quantity) } ?: emptyList(),
        drink = this.combo.drink?.map { OrderResponse.ComboResponse.ItemResponse(it.product.id, it.quantity) } ?: emptyList(),
        dessert = this.combo.dessert?.map { OrderResponse.ComboResponse.ItemResponse(it.product.id, it.quantity) } ?: emptyList()
    ),
    createdAt = this.createdAt.toBrazilianTime(),
    updatedAt = this.updatedAt?.toBrazilianTime(),
    completedAt = this.completedAt?.toBrazilianTime(),
    paymentStatus = this.paymentStatus.name,
    totals = OrderResponse.OrderTotalsResponse(
        totalSnacks = this.combo.totalSnacks(),
        totalGarnishes = this.combo.totalGarnishes(),
        totalDrinks = this.combo.totalDrinks(),
        totalDesserts = this.combo.totalDesserts(),
        totalOrder = calculateTotal()
    ),
    totalTime = formatDuration(this.totalTime()),
    statusTimes = this.statusTimes.mapValues { formatDuration( it.value)}
)