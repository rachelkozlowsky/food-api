package com.fiap.food

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.validation.annotation.Validated

@EnableWebSecurity
@SpringBootApplication
@Validated
@EnableMongoRepositories(basePackages = ["com.fiap.food.adapter.out.db"])
@ComponentScan(basePackages = ["com.fiap.food"])
class FoodApplication

fun main(args: Array<String>) {
	runApplication<FoodApplication>(*args)
}
