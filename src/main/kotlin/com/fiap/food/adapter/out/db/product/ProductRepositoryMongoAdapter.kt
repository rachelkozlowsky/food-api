package com.fiap.food.adapter.out.db.product

import com.fiap.food.application.domain.Product
import com.fiap.food.application.ports.out.ProductRepositoryPort
import com.fiap.food.utils.Loggable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository

@Repository
interface ProductMongoRepository : MongoRepository<ProductMongoEntity, String>

@Component
class ProductRepositoryMongoAdapter(
    private val repository: ProductMongoRepository
) : ProductRepositoryPort, Loggable {

    override fun save(product: Product): Product {
        logger.info("Saving product: $product")
        val productEntity = product.toMongoEntity()
        return repository.save(productEntity).toDomain()
    }

    override fun update(product: Product): Product {
        logger.info("Updating product: $product")
        val productEntity = product.toMongoEntity()
        return repository.save(productEntity).toDomain()
    }

    override fun delete(productId: String) {
        logger.info("Deleting product with id: $productId")
        repository.deleteById(productId)
    }

    override fun get(productId: String): Product {
        logger.info("Fetching product with id: $productId")
        return repository.findById(productId)
            .orElseThrow { NoSuchElementException("Product not found with id: $productId") }
            .toDomain()
    }

    override fun getAll(): List<Product> {
        logger.info("Fetching all products")
        return repository.findAll().map { it.toDomain() }
    }
}