package com.fiap.food.adapter.`in`.http.category

import com.fiap.food.application.domain.Category
import com.fiap.food.application.domain.enums.CategoryName
import com.fiap.food.application.service.CategoryService
import com.fiap.food.utils.dummyObject
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class CategoryControllerTest{
    private val categoryService = mockk<CategoryService>()
    private val categoryController = CategoryController(categoryService)

    private val response = dummyObject<Category>()

    @Test
    fun getAllReturnsOkWithValidRequest() {

        every { categoryService.getAll() } returns listOf( response)

        val result = categoryController.getAll()

        assertEquals(listOf(response), listOf(result))
        verify { categoryService.getAll() }
    }

    @Test
    fun getReturnsOkWithValidRequest() {
        val categoryId = "categoryId"

        every { categoryService.get(categoryId) } returns response

        val result = categoryController.get(categoryId)

        assertEquals(response.toResponse(), result)
        verify { categoryService.get(categoryId) }
    }

    @Test
    fun createReturnsOkWithValidRequest() {
        val categoryRequest = CategoryStaticRequest(
            CategoryName.DESSERT, "categoryDescription")

        every { categoryService.create(categoryRequest.toDomain()) } returns response

        val result = categoryController.create(categoryRequest)

        assertEquals(response.toResponse(), result)
        verify { categoryService.create(categoryRequest.toDomain()) }
    }

    @Test
    fun updateReturnsOkWithValidRequest() {
        val categoryId = "categoryId"
        val categoryRequest = CategoryStaticRequest(
            CategoryName.DESSERT, "categoryDescription")

        every { categoryService.update(categoryId, categoryRequest.toDomain()) } returns response

        val result = categoryController.update(categoryId, categoryRequest)

        assertEquals(response.toResponse(), result)
        verify { categoryService.update(categoryId, categoryRequest.toDomain()) }
    }

    @Test
    fun deleteReturnsOkWithValidRequest() {
        val categoryId = "categoryId"

        every { categoryService.delete(categoryId) } returns Unit

        categoryController.delete(categoryId)

        verify { categoryService.delete(categoryId) }
    }
}