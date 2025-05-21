package com.fiap.food.adapter.out.db.category

import com.fiap.food.application.domain.Category
import com.fiap.food.application.domain.exceptions.BadCredentialsException
import com.fiap.food.application.ports.out.CategoryRepositoryPort
import com.fiap.food.utils.Loggable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository

@Repository
interface CategoryMongoRepository : MongoRepository<CategoryMongoEntity, String>

@Component
class CategoryRepositoryMongoAdapter(
    private val repository: CategoryMongoRepository
) : CategoryRepositoryPort, Loggable {

    override fun save(category: Category): Category {
        logger.info("Saving category: $category")
        val categoryEntity = category.toMongoEntity()
        if (repository.existsById(categoryEntity.id)) {
            logger.error("Category with id ${categoryEntity.name} already exists")
            throw BadCredentialsException("Category with id ${categoryEntity.name
            } already exists. Please use a different name.")
        }
        return repository.save(categoryEntity).toDomain()
    }

    override fun update(categoryId: String, category: Category): Category {
        logger.info("Updating category: $categoryId")
        val existingCategory = repository.findById(categoryId)
            .orElseThrow { NoSuchElementException("Category not found with id: $categoryId") }

        val categotuToUpdate = existingCategory.copy(
            name = category.name,
            description = category.description,
            active = category.active
        )
        return repository.save(categotuToUpdate).toDomain()
    }

    override fun delete(categoryId: String) {
        logger.info("Deleting category with id: $categoryId")
        repository.findById(categoryId)
            .orElseThrow { NoSuchElementException("Category not found with id: $categoryId") }
        repository.deleteById(categoryId)
    }

    override fun get(categoryId: String): Category {
        logger.info("Fetching category with id: $categoryId")
        return repository.findById(categoryId)
            .orElseThrow { NoSuchElementException("Category not found with id: $categoryId") }
            .toDomain()
    }

    override fun getAll(): List<Category> {
        logger.info("Fetching all categories")
        return repository.findAll().map { it.toDomain() }
    }
}