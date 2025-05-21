package com.fiap.food.application.domain

data class Product(
    val id: String,
    val name: String,
    val description: String,
    val categoryId: String,
    val images: List<ImageData>,
    val price: Double,
    val active: Boolean = true
){
    data class ImageData(
        val url: String
    )
}


