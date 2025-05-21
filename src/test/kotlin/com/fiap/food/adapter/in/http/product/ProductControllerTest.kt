package com.fiap.food.adapter.`in`.http.product

import com.fiap.food.application.domain.Product
import com.fiap.food.application.service.ProductService
import com.fiap.food.utils.dummyObject
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class ProductControllerTest{
    private val productService = mockk<ProductService>()
    private val productController = ProductController(productService)

    private val response= dummyObject<Product>()

    @Test
    fun getAllReturnsOkWithValidRequest() {

        val responseAll = listOf(response)
        every { productService.getAll() } returns responseAll

        val result = productController.getAll()

        assertEquals(responseAll.map { it.toResponse() },result)
        verify { productService.getAll() }
    }

    @Test
    fun getReturnsOkWithValidRequest() {
        val productId = "productId"

        every { productService.get(productId) } returns response

        val result = productController.get(productId)

        assertEquals(response.toResponse(), result)
        verify { productService.get(productId) }
    }

    @Test
    fun createReturnsOkWithValidRequest() {
        val productRequest = mockk<ProductRequest>()

        every { productRequest.name } returns "eOMtThyhVNLWUZNRcBaQKxI"
        every { productRequest.toDomain() } returns dummyObject<Product>().copy(id = "b6d2e260-322c-4b76-a00f-ee5fc50a87c3")

        val expected = productRequest.toDomain()
        every { productService.create(expected) } returns expected

        val result = productController.create(productRequest)

        assertEquals(expected.toResponse(), result)
        verify { productService.create(expected) }
    }

    @Test
    fun updateReturnsOkWithValidRequest() {
        val productId = "productId"
        val productRequest = mockk<ProductRequest>()

        every { productRequest.name } returns "eOMtThyhVNLWUZNRcBaQKxI"
        every { productRequest.toDomain() } returns dummyObject<Product>().copy(id = "b6d2e260-322c-4b76-a00f-ee5fc50a87c3")

        val expected = productRequest.toDomain()
        every { productService.update(productId, expected) } returns expected

        val result = productController.update(productId, productRequest)

        assertEquals(expected.toResponse(), result)
        verify { productService.update(productId, expected) }
    }

    @Test
    fun deleteReturnsOkWithValidRequest() {
        val productId = "productId"

        every { productService.delete(productId) } returns Unit

        productController.delete(productId)

        verify { productService.delete(productId) }
    }

}