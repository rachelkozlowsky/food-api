package com.fiap.food.adapter.out.db.category

import com.fiap.food.application.domain.Category
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("Categories")
data class CategoryMongoEntity(
    @Id val id: String,
    var name: String,
    var description: String,
    var active: Boolean = true
)

fun CategoryMongoEntity.toDomain() = Category(
    id = this.id,
    name = this.name,
    description = this.description,
    active = this.active
)

fun Category.toMongoEntity() = CategoryMongoEntity(
    id = this.id,
    name = this.name,
    description = this.description,
    active = this.active
)