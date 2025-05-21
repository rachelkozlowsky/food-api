package com.fiap.food.adapter.out.db.product

import com.fiap.food.application.domain.Product
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("Products")
data class ProductMongoEntity(
    @Id val id: String,
    val name: String,
    val description: String,
    val categoryId: String,
    val images: List<ImageData>,
    val price: Double,
    val active: Boolean
) {
    data class ImageData(
        val url: String
    )
}

fun Product.ImageData.toMongoEntity() = ProductMongoEntity.ImageData(
    this.url
)

fun ProductMongoEntity.ImageData.toDomain() = Product.ImageData(
    this.url
)

fun ProductMongoEntity.toDomain() = Product(
    this.id,
    this.name,
    this.description,
    this.categoryId,
    this.images.map { Product.ImageData(it.url) },
    this.price,
    this.active
)

fun Product.toMongoEntity() = ProductMongoEntity(
    this.id,
    this.name,
    this.description,
    this.categoryId,
    this.images.map { ProductMongoEntity.ImageData(it.url) },
    this.price,
    this.active
)