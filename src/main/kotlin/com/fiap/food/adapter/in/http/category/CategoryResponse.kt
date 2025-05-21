package com.fiap.food.adapter.`in`.http.category

import com.fiap.food.application.domain.Category

data class CategoryResponse(
    val id: String,
    val name: String,
    val description: String,
    val active: Boolean
)

fun Category.toResponse() = CategoryResponse(
    id = this.id,
    name = this.name,
    description = this.description,
    active = this.active
)