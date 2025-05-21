package com.fiap.food.application.ports.out

import com.fiap.food.application.domain.Category

interface CategoryRepositoryPort {
    fun save(category: Category): Category
    fun update(categoryId: String, category: Category): Category
    fun delete(categoryId: String)
    fun get(categoryId: String): Category
    fun getAll(): List<Category>
}