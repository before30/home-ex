package cc.before30.home.logging

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class LoggingApplication : CommandLineRunner {
    companion object {
        private val logger = LoggerFactory.getLogger(LoggingApplication::class.java)
    }

    override fun run(vararg args: String?) {
        logger.info("hello world")
    }
}

fun main(args: Array<String>) {
    runApplication<LoggingApplication>(*args)
}