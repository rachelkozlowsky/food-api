package com.fiap.food.adapter.`in`.http.category

import com.fiap.food.annotation.EnumValidator
import com.fiap.food.application.domain.Category
import com.fiap.food.application.domain.enums.CategoryName

data class CategoryRequest ( //permite a criação de novas categorias
    val name: String,
    val description: String,
    val active: Boolean = true
){
    fun toDomain() = Category(
        id = this.name.lowercase(),
        name = this.name.uppercase(),
        description = this.description,
        active = this.active
    )
}

data class CategoryStaticRequest( // trava com os enum existentes
    @EnumValidator(enumClazz = CategoryName::class, required = true)
    val name: CategoryName,
    val description: String,
    val active: Boolean? = true
)
{
    fun toDomain() = Category(
        id = this.name.name.lowercase(),
        name = this.name.name.uppercase(),
        description = this.description,
        active = this.active!!
    )
}