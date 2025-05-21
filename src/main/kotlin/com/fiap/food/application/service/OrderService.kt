package com.fiap.food.application.service

import com.fiap.food.application.domain.Order
import com.fiap.food.application.domain.enums.OrderStatus
import com.fiap.food.application.ports.out.OrderRepositoryPort
import com.fiap.food.application.ports.out.ProductRepositoryPort
import com.fiap.food.application.ports.out.UserRepositoryPort
import com.fiap.food.utils.Loggable
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

@Component
class OrderService(
    private val orderRepository: OrderRepositoryPort,
    private val userRepository: UserRepositoryPort,
    private val productRepository: ProductRepositoryPort
): Loggable {

    fun create(cpf: String, comboItens: List<Pair<String, Int>>): Order{
        logger.info("Creating order for user with CPF: $cpf")

        val user = userRepository.get(cpf)
        val combo = comboItens.map { (productId, quantity) ->
            val product = productRepository.get(productId)
        Order.Combo.Item(product, quantity)
        }

        val comboDomain = Order.Combo(
            snack = combo.filter{it.product.categoryId == "snack"},
            garnish = combo.filter{it.product.name == "garnish"},
            drink = combo.filter{it.product.name == "drink"},
            dessert = combo.filter{it.product.name == "dessert"}
        )
        val total = comboDomain.totalSnacks() + comboDomain.totalGarnishes() + comboDomain.totalDrinks() + comboDomain.totalDesserts()

        val order = Order(
            id = UUID.randomUUID().toString(),
            status = OrderStatus.RECEIVED,
            user = user?.cpf,
            combo = comboDomain,
            total = total,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
        )
        logger.info("Order created: $order")
        return orderRepository.save(order)

    }


    fun update(orderId: String, order: Order): Order {
        logger.info("Updating order with ID: $orderId")

        val existingOrder = orderRepository.get(orderId)

        val updatedOrder = order.copy(
            id = existingOrder.id,
            createdAt = existingOrder.createdAt
        )
        return orderRepository.update(updatedOrder)
    }

    fun updateOrderStatus(orderId: String, newStatus: OrderStatus) {
        logger.info("Updating order status for order with ID: $orderId to $newStatus")
        val order = orderRepository.get(orderId)
        order.let {
            it.updateStatus(newStatus)
            orderRepository.save(it)
        }
    }

    fun delete(orderId: String) {
        logger.info("Deleting order with ID: $orderId")
        val order = orderRepository.get(orderId)

        orderRepository.delete(order.id)
    }

    fun get(orderId: String): Order {
        logger.info("Getting order with ID: $orderId")
        return orderRepository.get(orderId)
    }

    fun getByStatus(status: OrderStatus): List<Order> {
        logger.info("Getting orders with status: $status")
        return orderRepository.getByStatus(status)
    }

    fun getAll(): List<Order> {
        logger.info("Getting all orders")
        return orderRepository.getAll()
    }
}
