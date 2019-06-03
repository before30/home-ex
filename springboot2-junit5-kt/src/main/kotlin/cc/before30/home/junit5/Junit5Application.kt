package cc.before30.home.junit5

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Junit5Application

fun main(args: Array<String>) {
    runApplication<Junit5Application>(*args)
}