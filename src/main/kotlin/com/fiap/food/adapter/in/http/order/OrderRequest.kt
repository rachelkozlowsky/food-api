package com.fiap.food.adapter.`in`.http.order

import com.fiap.food.annotation.EnumValidator
import com.fiap.food.application.domain.Order
import com.fiap.food.application.domain.Product
import com.fiap.food.application.domain.enums.OrderStatus
import java.time.LocalDateTime
import java.util.*


data class OrderRequest (
    @EnumValidator(enumClazz = OrderStatus::class, required = false)
    val status: OrderStatus?,
    val user: String?,
    val combo: ComboRequest
){
    data class ComboRequest(
        val snack: List<ItemRequest>? = null,
        val garnish: List<ItemRequest>? = null,
        val drink: List<ItemRequest>? = null,
        val dessert: List<ItemRequest>? = null
    ){

        fun toDomainItems(): List<Pair<String, Int>> {
            return (this.snack.orEmpty() + this.garnish.orEmpty() + this.drink.orEmpty() + this.dessert.orEmpty())
                .map { it.productId to it.quantity }
        }
        data class ItemRequest(
            val productId: String,
            val quantity: Int
        ){
            fun toDomain(findProduct: (String) -> Product) = Order.Combo.Item(
                product = findProduct(this.productId),
                quantity = this.quantity
            )
        }
        fun toDomain(findProduct: (String) -> Product) = Order.Combo(
            snack = this.snack?.map { it.toDomain(findProduct) } ?: emptyList(),
            garnish = this.garnish?.map { it.toDomain(findProduct) } ?: emptyList(),
            drink = this.drink?.map { it.toDomain(findProduct) } ?: emptyList(),
            dessert = this.dessert?.map { it.toDomain(findProduct) } ?: emptyList()
        )
    }
    fun toDomain(findProduct: (String) -> Product) = Order(
        id = UUID.randomUUID().toString(),
        status = this.status ?: OrderStatus.RECEIVED,
        user = this.user,
        combo = this.combo.toDomain(findProduct),
        total = 0.0,
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now(),
        completedAt = null
    )


}