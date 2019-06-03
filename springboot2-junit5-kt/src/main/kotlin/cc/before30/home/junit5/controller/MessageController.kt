package cc.before30.home.junit5.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MessageController {
    @GetMapping("/hello2")
    fun hello2() = "world2"
}
