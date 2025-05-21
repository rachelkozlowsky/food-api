package com.fiap.food.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    @Bean
    fun openAPI(): OpenAPI = OpenAPI().info(
        Info().title("Sistema de Pedidos")
            .version("v1")
            .description("APIs para gestão de clientes, produtos e pedidos")
    )
}