package com.example.demo.service

import com.example.demo.system.*
import org.springframework.stereotype.Service

@Service // Позволяем IoC контейнеру внедрять класс
class ProductService(private val productRepository: ProductRepository) { // Внедряем репозиторий в качестве зависимости
    fun all(): Iterable<Product> = productRepository.findAll() // Возвращаем коллекцию сушьностей, Функциональная запись с указанием типа

    fun get(id: Long): Product = productRepository.findOne(id)

    fun add(product: Product): Product = productRepository.save(product)

    fun edit(id: Long, product: Product): Product = productRepository.save(product.copy(id = id)) // Сохраняем копию объекта с указанным id в БД

    fun remove(id: Long) = productRepository.delete(id)
}