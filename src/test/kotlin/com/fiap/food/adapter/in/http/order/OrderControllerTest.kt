package com.fiap.food.adapter.`in`.http.order

import com.fiap.food.application.domain.Order
import com.fiap.food.application.domain.enums.OrderStatus
import com.fiap.food.application.service.OrderService
import com.fiap.food.application.service.ProductService
import org.junit.jupiter.api.Assertions.*
import com.fiap.food.utils.dummyObject
import com.mercadopago.resources.order.OrderItem
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class OrderControllerTest{
    private val orderService = mockk<OrderService>()
    private val productService = mockk<ProductService>()
    private val orderController = OrderController(orderService, productService)

    private val response= dummyObject<Order>()

    @Test
    fun getAllReturnsOkWithValidRequest() {

        val responseAll = listOf(response)
        every { orderService.getAll() } returns responseAll

        val result = orderController.getAll()

        assertEquals(responseAll.map { it.toResponse() },result)
        verify { orderService.getAll() }
    }

    @Test
    fun getReturnsOkWithValidRequest() {
        val orderId = "orderId"

        every { orderService.get(orderId) } returns response

        val result = orderController.get(orderId)

        assertEquals(response.toResponse(), result)
        verify { orderService.get(orderId) }
    }

    @Test
    fun createReturnsOkWithValidRequest() {
        val orderRequest = mockk<OrderRequest>()
        val orderItems = listOf(dummyObject<OrderItem>())
        val comboItems = orderItems.map { it.id to it.quantity }
        val expectedOrder = dummyObject<Order>().copy(id = "orderId")

        every { orderRequest.user } returns "userCpf"
        every { orderRequest.combo.toDomainItems() } returns comboItems
        every { orderService.create("userCpf", comboItems) } returns expectedOrder

        val result = orderController.create(orderRequest)

        assertEquals(expectedOrder.toResponse(), result)
        verify { orderService.create("userCpf", comboItems) }

    }

    @Test
    fun updateReturnsOkWithValidRequest() {
        val orderId = "orderId"
        val orderRequest = mockk<OrderRequest>()
        val updatedOrder = dummyObject<Order>()

        every { orderRequest.toDomain(any()) } returns updatedOrder
        every { orderService.update(orderId, updatedOrder) } returns updatedOrder

        val result = orderController.update(orderId, orderRequest)

        assertEquals(updatedOrder.toResponse(), result)
        verify { orderService.update(orderId, updatedOrder) }
    }

    @Test
    fun updateThrowsExceptionWithInvalidRequest() {
        val orderId = "orderId"
        val orderRequest = mockk<OrderRequest>()
        val updatedOrder = dummyObject<Order>()

        every { orderRequest.toDomain(any()) } returns updatedOrder
        every { orderService.update(orderId, updatedOrder) } throws IllegalArgumentException("Invalid Order")

        assertThrows<IllegalArgumentException> {
            orderController.update(orderId, orderRequest)
        }
    }

    @Test
    fun updateOrderStatusThrowsExceptionWithInvalidRequest() {
        val orderId = "orderId"
        val request = mockk<OrderStatusUpdateRequest>()
        val newStatus = OrderStatus.RECEIVED

        every { request.newStatus } returns newStatus
        every { orderService.updateOrderStatus(orderId, newStatus) } throws IllegalArgumentException("Invalid Order Status")

        assertThrows<IllegalArgumentException> {
            orderController.updateOrderStatus(orderId, request)
        }
    }

    @Test
    fun getByStatusReturnsOkWithValidRequest() {
        val status = OrderStatus.IN_PREPARATION
        val responseAll = listOf(response)

        every { orderService.getByStatus(status) } returns responseAll

        val result = orderController.getByStatus(status)

        assertEquals(responseAll.map { it.toResponse() }, result)
        verify { orderService.getByStatus(status) }
    }

    @Test
    fun getByStatusThrowsExceptionWithInvalidRequest() {
        val status = OrderStatus.IN_PREPARATION

        every { orderService.getByStatus(status) } throws IllegalArgumentException("Invalid Order Status")

        assertThrows<IllegalArgumentException> {
            orderController.getByStatus(status)
        }
    }

    @Test
    fun updateOrderStatusThrowsExceptionWithInvalidOrder() {
        val orderId = "orderId"
        val request = mockk<OrderStatusUpdateRequest>()
        val newStatus = OrderStatus.RECEIVED

        every { request.newStatus } returns newStatus
        every { orderService.updateOrderStatus(orderId, newStatus) } throws IllegalArgumentException("Invalid Order")

        assertThrows<IllegalArgumentException> {
            orderController.updateOrderStatus(orderId, request)
        }
    }

    @Test
    fun getByStatusThrowsExceptionWithInvalidOrder() {
        val status = OrderStatus.RECEIVED

        every { orderService.getByStatus(status) } throws IllegalArgumentException("Invalid Order")

        assertThrows<IllegalArgumentException> {
            orderController.getByStatus(status)
        }
    }

    @Test
    fun getByStatusThrowsExceptionWithInvalidProduct() {
        val status = OrderStatus.RECEIVED

        every { orderService.getByStatus(status) } throws IllegalArgumentException("Invalid Product")

        assertThrows<IllegalArgumentException> {
            orderController.getByStatus(status)
        }
    }
}