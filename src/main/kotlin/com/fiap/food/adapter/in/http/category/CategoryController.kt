package com.fiap.food.adapter.`in`.http.category

import com.fiap.food.annotation.Roles
import com.fiap.food.application.domain.enums.UserRole.ADMIN
import com.fiap.food.application.service.CategoryService
import com.fiap.food.utils.Loggable
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/categories")
@CrossOrigin(origins = ["*"])
class CategoryController(
    val categoryService: CategoryService
): Loggable {

    @GetMapping("/{categoryId}")
    fun get(@PathVariable categoryId: String ): CategoryResponse {
        logger.info("Get category with id: $categoryId")
        return categoryService.get(categoryId).toResponse()
    }

    @GetMapping
    fun getAll(): List<CategoryResponse> {
        logger.info("Get all categories")
        return categoryService.getAll().map { it.toResponse() }
    }

    @Roles(ADMIN)
    @PostMapping
    fun create(@Valid @RequestBody categoryRequest: CategoryStaticRequest): CategoryResponse {
        logger.info("Create category with name: ${categoryRequest.name}")
        return categoryService.create(categoryRequest.toDomain()).toResponse()
    }

    @Roles(ADMIN)
    @PutMapping("/{categoryId}")
    fun update(@PathVariable categoryId: String, @Valid @RequestBody categoryRequest: CategoryStaticRequest): CategoryResponse {
        logger.info("Update category with id: $categoryId")
        return categoryService.update(categoryId, categoryRequest.toDomain()).toResponse()
    }

    @Roles(ADMIN)
    @DeleteMapping("/{categoryId}")
    fun delete(@PathVariable categoryId: String) {
        logger.info("Delete category with id: $categoryId")
        categoryService.delete(categoryId)
    }
}
