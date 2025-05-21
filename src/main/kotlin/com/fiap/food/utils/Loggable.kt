package com.fiap.food.utils

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

interface Loggable {
    val logger: Logger
        get() = LogManager.getLogger(this.javaClass)
}