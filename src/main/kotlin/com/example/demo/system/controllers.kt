package com.example.demo.system

import com.example.demo.service.*
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController // Сообщаем как обрабатывать http запросы и в каком виде отправлять ответы (сериализация в JSON и обратно)
@RequestMapping("products") // Указываем перфик маршруета для всех экшенов
class ProductsController(private val productService: ProductService) { // Внедряем наш сервис в качестве зависимости
    @GetMapping // Говорим что экшен принемает GET запрос без параметров в url
    fun index() = productService.all() // И возвращает результат метода all нашего сервиса. Функциональная запись с выводом типа

    @PostMapping // Экшен принемает POST запрос без параметров в url
    @ResponseStatus(HttpStatus.CREATED) // Указываем специфический HttpStatus при успешном ответе
    fun create(@RequestBody product: Product) = productService.add(product) // Принемаем объект Product из тела запроса и передаем его в метод add нашего сервиса

    @GetMapping("{id}") // Тут мы говорим что при PUT запросе url должен содержать id (http://localhost/products/{id})
    @ResponseStatus(HttpStatus.FOUND)
    fun read(@PathVariable id: Long) = productService.get(id) // Сообщаем что наш id типа Long и передаем его в метод get сервиса

    @PutMapping("{id}")
    fun update(@PathVariable id: Long, @RequestBody product: Product) = productService.edit(id, product) // Здесь мы принемаем один параметр из url, второй из тела PUT запроса и отдаем их методу edit

    @DeleteMapping("{id}")
    fun delete(@PathVariable id: Long) = productService.remove(id)
}