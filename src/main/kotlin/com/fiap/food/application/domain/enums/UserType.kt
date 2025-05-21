package com.fiap.food.application.domain.enums

enum class UserType {
    IDENTIFIED,
    ANONYMOUS;

    override fun toString(): String = name
}