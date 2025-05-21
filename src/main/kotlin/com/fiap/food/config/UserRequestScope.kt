package com.fiap.food.config

import com.fiap.food.application.domain.User
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.web.context.annotation.RequestScope

open class UserRequestScope {

    var user: User? = null
}

@Component
class ScopeConfig(){
    @Bean
    @RequestScope
    fun requestUser(): UserRequestScope = UserRequestScope()
}