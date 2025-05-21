package com.fiap.food.application.ports.out

import com.fiap.food.application.domain.Product

interface ProductRepositoryPort {
    fun save(product: Product): Product
    fun update(product: Product): Product
    fun delete(productId: String)
    fun get(productId: String): Product
    fun getAll(): List<Product>
}