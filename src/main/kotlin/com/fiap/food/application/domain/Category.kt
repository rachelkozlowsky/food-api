package com.fiap.food.application.domain

data class Category(
    val id: String,
    val name: String,
    val description: String,
    val active: Boolean = true
)