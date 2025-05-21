package com.fiap.food.application.service

import com.fiap.food.application.domain.Category
import com.fiap.food.application.ports.out.CategoryRepositoryPort
import com.fiap.food.utils.Loggable
import org.springframework.stereotype.Component

@Component
class CategoryService(
    private val repository: CategoryRepositoryPort
): Loggable {

    fun create(category: Category): Category {
        logger.info("Creating category: $category")
        return repository.save(category)
    }

    fun update(categoryId: String, category: Category): Category {
        logger.info("Updating category with ID: $categoryId")
        return repository.update(categoryId, category)
    }

    fun delete(categoryId: String) {
        logger.info("Deleting category with ID: $categoryId")
        repository.delete(categoryId)
    }

    fun get(categoryId: String): Category {
        logger.info("Getting category with ID: $categoryId")
        return repository.get(categoryId.lowercase())
    }

    fun getAll(): List<Category> {
        logger.info("Getting all categories")
        return repository.getAll()
    }
}