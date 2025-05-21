package com.fiap.food.adapter.`in`.http.product

import com.fiap.food.application.domain.Product

data class ProductResponse(
    val id: String,
    val name: String,
    val description: String,
    val categoryId: String,
    val images: List<ImageResponse>,
    val price: Double,
    val active: Boolean
){
    data class ImageResponse(
        val url: String
    )
}

fun Product.toResponse() = ProductResponse(
    id = this.id,
    name = this.name,
    description = this.description,
    categoryId = this.categoryId,
    images = this.images.map { ProductResponse.ImageResponse(it.url) },
    price = this.price,
    active = this.active
)