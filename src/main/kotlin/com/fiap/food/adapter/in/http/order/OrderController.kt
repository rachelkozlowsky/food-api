package com.fiap.food.adapter.`in`.http.order

import com.fiap.food.application.domain.enums.OrderStatus
import com.fiap.food.application.service.OrderService
import com.fiap.food.application.service.ProductService
import com.fiap.food.utils.Loggable
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/orders")
@CrossOrigin(origins = ["*"])
class OrderController(
    val orderService: OrderService,
    val productService: ProductService
): Loggable {

    @GetMapping("/{orderId}")
    fun get(@PathVariable orderId: String): OrderResponse {
        logger.info("Get order with id: $orderId")
        return orderService.get(orderId).toResponse()
    }

    @GetMapping
    fun getAll(): List<OrderResponse> {
        logger.info("Get all orders")
        return orderService.getAll().map { it.toResponse() }
    }

    @GetMapping("/status/{status}")
    fun getByStatus(@PathVariable status: OrderStatus): List<OrderResponse> {
        logger.info("Get orders with status: $status")
        return orderService.getByStatus(status).map { it.toResponse() }
    }

    @PostMapping
    fun create(@Valid @RequestBody orderRequest: OrderRequest): OrderResponse {
        logger.info("Create order with user: ${orderRequest.user}")
        val cpf = orderRequest.user ?: "Anonymous"

        val comboItems = orderRequest.combo.toDomainItems()

        return orderService.create(cpf, comboItems).toResponse()
    }

    @PutMapping("/{orderId}")
    fun update(@PathVariable orderId: String, @Valid @RequestBody orderRequest: OrderRequest): OrderResponse {
        logger.info("Update order with id: $orderId")
        val updatedOrder = orderRequest.toDomain { productId ->
            productService.get(productId)
        }
        return orderService.update(orderId, updatedOrder).toResponse()
    }

    @PutMapping("/{orderId}/status")
    fun updateOrderStatus(@PathVariable orderId: String, @RequestBody request: OrderStatusUpdateRequest) {
        logger.info("Update order status with id: $orderId to ${request.newStatus}")
        orderService.updateOrderStatus(orderId, request.newStatus)
    }

}
