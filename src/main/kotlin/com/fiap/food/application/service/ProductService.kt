package com.fiap.food.application.service

import com.fiap.food.application.domain.Product
import com.fiap.food.application.ports.out.ProductRepositoryPort
import com.fiap.food.utils.Loggable
import org.springframework.stereotype.Component

@Component
class ProductService(
    private val repository: ProductRepositoryPort,
    private val categoryService: CategoryService
): Loggable {

    fun create(product: Product): Product{
        logger.info("Creating product: $product")
        val category = categoryService.get(product.categoryId)
        val productToSave = product.copy(categoryId = category.id)
        return repository.save(productToSave)
    }

    fun update(productId: String, product: Product): Product {
        logger.info("Updating product with ID: $productId")
        val category = categoryService.get(product.categoryId)
        val productToSave = product.copy(categoryId = category.id)
        return repository.update(productToSave)
    }

    fun delete(productId: String) {
        logger.info("Deleting product with ID: $productId")
        val product = repository.get(productId)
        categoryService.delete(product.categoryId)
    }

    fun get(productId: String): Product {
        logger.info("Getting product with ID: $productId")
        return repository.get(productId)
    }

    fun getAll(): List<Product> {
        logger.info("Getting all products")
        return repository.getAll()
    }

    fun getByCategory(categoryId: String): List<Product> {
        logger.info("Getting products by category ID: $categoryId")
        return repository.getAll().filter { it.categoryId == categoryId }
    }
}