package com.fiap.food.adapter.`in`.http.product

import com.fasterxml.jackson.annotation.JsonProperty
import com.fiap.food.annotation.EnumValidator
import com.fiap.food.application.domain.Product
import com.fiap.food.application.domain.enums.CategoryName
import java.util.*

data class ProductRequest (
    val name: String,
    val description: String,
    @EnumValidator(enumClazz = CategoryName::class, required = true)
    val categoryId: String,
    val images: List<ImageRequest>,
    val price: Double,
    val active: Boolean? = true
){

    data class ImageRequest(
        @JsonProperty("url")
        val url: String
    )

    fun toDomain() = Product(
        id = UUID.randomUUID().toString(),
        name = this.name,
        description = this.description,
        categoryId = this.categoryId.toString(),
        images = this.images.map { Product.ImageData(it.url) },
        price = this.price,
        active = this.active ?: true
    )
}