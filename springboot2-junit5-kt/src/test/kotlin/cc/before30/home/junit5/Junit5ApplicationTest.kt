package cc.before30.home.junit5

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class Junit5ApplicationTest {
    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    @Test
    @DisplayName("test Message REST API")
    fun `test Message REST API`() {
        val message = testRestTemplate.getForObject("/hello2", String::class.java)
        assertEquals("world2", message)
    }
}