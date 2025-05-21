package com.fiap.food.adapter.out.db.order

import com.fiap.food.application.domain.Order
import com.fiap.food.application.domain.Order.Combo
import com.fiap.food.application.domain.enums.OrderStatus
import com.fiap.food.application.domain.enums.PaymentStatus
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("Orders")
data class OrderMongoEntity(
    @Id val id: String,
    var status: OrderStatus,
    val user: String?,
    val combo: Combo,
    var total: Double = 0.0,
    var paymentStatus: PaymentStatus = PaymentStatus.PENDING,
    val createdAt: LocalDateTime,
    var updatedAt: LocalDateTime? = null,
    val completedAt: LocalDateTime? = null,
    var statusUpdatedAt: LocalDateTime? = null,
    var statusTimes: MutableMap<OrderStatus, java.time.Duration> = mutableMapOf()
)

fun OrderMongoEntity.toDomain() = Order(
    id = this.id,
    status = this.status,
    user = this.user,
    combo = Order.Combo(
        snack = this.combo.snack,
        garnish = this.combo.garnish,
        drink = this.combo.drink,
        dessert = this.combo.dessert
    ),
    total = this.total,
    paymentStatus = this.paymentStatus,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
    completedAt = this.completedAt,
    statusUpdatedAt = this.statusUpdatedAt,
    statusTimes = this.statusTimes
)

fun Order.toMongoEntity() =OrderMongoEntity(
    id = this.id,
    status = this.status,
    user = this.user,
    combo = Order.Combo(
        snack = this.combo.snack,
        garnish = this.combo.garnish,
        drink = this.combo.drink,
        dessert = this.combo.dessert
    ),
    total = this.total,
    paymentStatus = this.paymentStatus,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
    completedAt = this.completedAt,
    statusUpdatedAt = this.statusUpdatedAt,
    statusTimes = this.statusTimes
)