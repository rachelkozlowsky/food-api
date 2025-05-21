package com.fiap.food.adapter.out.db.payment

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface PaymentMongoRepository : MongoRepository<PaymentMongoEntity, String>