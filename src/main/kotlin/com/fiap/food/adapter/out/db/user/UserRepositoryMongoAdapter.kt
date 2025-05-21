package com.fiap.food.adapter.out.db.user

import com.fiap.food.application.domain.User
import com.fiap.food.application.ports.out.UserRepositoryPort
import com.fiap.food.utils.Loggable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import kotlin.jvm.optionals.getOrNull

@Repository
interface UserMongoRepository : MongoRepository<UserMongoEntity, String>

@Component
class UserRepositoryMongoAdapter(
    private val repository: UserMongoRepository
) : UserRepositoryPort, Loggable {

    override fun save(user: User): User {
        logger.info("Saving user: $user")
        val userEntity = user.toMongoEntity()
        return repository.save(userEntity).toDomain()
    }

    override fun update(user: User): User {
        logger.info("Updating user: $user")
        val userEntity = user.toMongoEntity()
        return repository.save(userEntity).toDomain()
    }

    override fun delete(userId: String) {
        logger.info("Deleting user with id: $userId")
        repository.deleteById(userId)
    }

    override fun get(userId: String): User? {
        logger.info("Fetching user with id: $userId")
        return repository.findById(userId).getOrNull()?.toDomain()
    }

    override fun getAll(): List<User> {
        logger.info("Fetching all users")
        return repository.findAll().map { it.toDomain() }
    }
}