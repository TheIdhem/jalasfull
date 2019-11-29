package ir.faj.jalas.jalas.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1.0")
class SessionController {

    @GetMapping("/session")
    fun getSession():String{
        return "test"
    }
}