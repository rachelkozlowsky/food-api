package com.fiap.food.adapter.`in`.http.product

import com.fiap.food.annotation.Roles
import com.fiap.food.application.domain.enums.UserRole.ADMIN
import com.fiap.food.application.service.ProductService
import com.fiap.food.utils.Loggable
import jakarta.validation.Valid
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/products")
@Validated
@CrossOrigin(origins = ["*"])

class ProductController(
    val productService: ProductService
): Loggable {

    @GetMapping("/{productId}")
    fun get(@PathVariable productId: String): ProductResponse {
        logger.info("Get product with id: $productId")
        return productService.get(productId).toResponse()
    }

    @GetMapping("/categories/{categoryId}")
    fun getByCategory(@PathVariable categoryId: String): List<ProductResponse> {
        logger.info("Get products with category id: $categoryId")
        return productService.getByCategory(categoryId).map { it.toResponse() }
    }

    @GetMapping
    fun getAll(): List<ProductResponse> {
        logger.info("Get all products")
        return productService.getAll().map { it.toResponse() }
    }

    @Roles(ADMIN)
    @PostMapping
    fun create(@Valid @RequestBody productRequest: ProductRequest): ProductResponse {
        logger.info("Create product with name: ${productRequest.name}")
        return productService.create(productRequest.toDomain()).toResponse()
    }

    @Roles(ADMIN)
    @PutMapping("/{productId}")
    fun update(@PathVariable productId: String, @Valid @RequestBody productRequest: ProductRequest): ProductResponse {
        logger.info("Update product with id: $productId")
        return productService.update(productId, productRequest.toDomain()).toResponse()
    }

    @Roles(ADMIN)
    @DeleteMapping("/{productId}")
    fun delete(@PathVariable productId: String) {
        logger.info("Delete product with id: $productId")
        productService.delete(productId)
    }
}
