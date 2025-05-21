package com.fiap.food.adapter.out.db.order

import com.fiap.food.application.domain.Order
import com.fiap.food.application.domain.enums.OrderStatus
import com.fiap.food.application.ports.out.OrderRepositoryPort
import com.fiap.food.utils.Loggable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository

@Repository
interface OrderMongoRepository : MongoRepository<OrderMongoEntity, String>{
    fun findAllByOrderByCreatedAtAsc(): List<OrderMongoEntity>
    fun findByStatusOrderByCreatedAtAsc(status: OrderStatus): List<OrderMongoEntity>
}

@Component
class OrderRepositoryMongoAdapter(
    private val repository: OrderMongoRepository
) : OrderRepositoryPort, Loggable {

    override fun save(order: Order): Order {
        logger.info("Saving order: $order")
        val orderEntity = order.toMongoEntity()
        return repository.save(orderEntity).toDomain()
    }

    override fun update(order: Order): Order {
        logger.info("Updating order: $order")
        val orderEntity = order.toMongoEntity()
        return repository.save(orderEntity).toDomain()
    }

    override fun delete(orderId: String) {
        logger.info("Deleting order with id: $orderId")
        repository.deleteById(orderId)
    }

    override fun get(orderId: String): Order {
        logger.info("Fetching order with id: $orderId")
        return repository.findById(orderId)
            .orElseThrow { NoSuchElementException("Order not found with id: $orderId") }
            .toDomain()
    }

    override fun getByStatus(status: OrderStatus): List<Order> {
        logger.info("Fetching orders with status: $status")
        return repository.findByStatusOrderByCreatedAtAsc(status).map { it.toDomain() }
    }

    override fun getAll(): List<Order> {
        logger.info("Fetching all orders")
        return repository.findAllByOrderByCreatedAtAsc().map { it.toDomain() } ?: emptyList()
    }
}