package com.fiap.food.adapter.out.http.client.mercadopago

import com.mercadopago.MercadoPagoConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct

@Configuration
class MercadoPagoConfig {
    @Value("\${mercadopago.access.token}")
    private val accessToken: String? = null

    @PostConstruct
    fun init() {
        MercadoPagoConfig.setAccessToken(accessToken)
    }
}