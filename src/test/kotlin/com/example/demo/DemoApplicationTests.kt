package com.example.demo

import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.web.context.WebApplicationContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.setup.MockMvcBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*

@SpringBootTest
@RunWith(SpringRunner::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING) // Запускать тесты в алфавитном порядке
class DemoApplicationTests {
    private val baseUrl = "http://localhost:8080/products/"
    private val jsonContentType = MediaType(MediaType.APPLICATION_JSON.type, MediaType.APPLICATION_JSON.subtype) // Записываем http заголовок в переменную для удобства
    private lateinit var mockMvc: MockMvc // Объявляем изменяемую переменную с отложенной инициализацией в которой будем хранить mock объект

    @Autowired
    private lateinit var webAppContext: WebApplicationContext // Объявляем изменяемую переменную с отложенной инициализацией в которую будет внедрен конекст приложения

    @Before // Этот метод будет запущен перед каждым тестом
    fun before() {
        mockMvc = webAppContextSetup(webAppContext).build() // Создаем объект с контекстом придожения
    }

    @Test
    fun `1 - Get empty list of products`() { // Так можно красиво называть методы
        val request = get(baseUrl).contentType(jsonContentType) // Создаем GET запрос по адресу http://localhost:8080/products/ с http заголовком Content-Type: application/json

        mockMvc.perform(request) // Выполняем запрос
                .andExpect(status().isOk) // Ожидаем http статус 200 OK
                .andExpect(content().json("[]", true)) // ожидаем пустой JSON массив в теле ответа
    }

    @Test
    fun `2 - Add first product`() {
        val passedJsonString = """
            {
                "name": "iPhone 4S",
                "description": "Mobile phone by Apple"
            }
        """.trimIndent()

        val request = post(baseUrl).contentType(jsonContentType).content(passedJsonString)

        val resultJsonString = """
            {
                "name": "iPhone 4S",
                "description": "Mobile phone by Apple",
                "id": 1
            }
        """.trimIndent()

        mockMvc.perform(request)
                .andExpect(status().isCreated)
                .andExpect(content().json(resultJsonString, true))
    }

    @Test
    fun `3 - Update first product`() {
        val passedJsonString = """
            {
                "name": "iPhone 4S",
                "description": "Smart phone by Apple"
            }
        """.trimIndent()

        val request = put(baseUrl + "1").contentType(jsonContentType).content(passedJsonString)

        val resultJsonString = """
            {
                "name": "iPhone 4S",
                "description": "Smart phone by Apple",
                "id": 1
            }
        """.trimIndent()

        mockMvc.perform(request)
                .andExpect(status().isOk)
                .andExpect(content().json(resultJsonString, true))
    }

    @Test
    fun `4 - Get first product`() {
        val request = get(baseUrl + "1").contentType(jsonContentType)

        val resultJsonString = """
            {
                "name": "iPhone 4S",
                "description": "Smart phone by Apple",
                "id": 1
            }
        """.trimIndent()

        mockMvc.perform(request)
                .andExpect(status().isFound)
                .andExpect(content().json(resultJsonString, true))
    }

    @Test
    fun `5 - Get list of products, with one product`() {
        val request = get(baseUrl).contentType(jsonContentType)

        val resultJsonString = """
            [
                {
                    "name": "iPhone 4S",
                    "description": "Smart phone by Apple",
                    "id": 1
                }
            ]
        """.trimIndent()

        mockMvc.perform(request)
                .andExpect(status().isOk)
                .andExpect(content().json(resultJsonString, true))
    }

    @Test
    fun `6 - Delete first product`() {
        val request = delete(baseUrl + "1").contentType(jsonContentType)

        mockMvc.perform(request).andExpect(status().isOk)
    }

}
