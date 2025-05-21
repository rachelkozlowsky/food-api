package com.fiap.food.utils

import org.jeasy.random.EasyRandom
import org.jeasy.random.EasyRandomParameters

 inline fun<reified T> dummyObject(): T = EasyRandom(EasyRandomParameters().ignoreRandomizationErrors(true)).nextObject(T::class.java)