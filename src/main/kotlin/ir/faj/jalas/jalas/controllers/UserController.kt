package ir.faj.jalas.jalas.controllers

import ir.faj.jalas.jalas.controllers.model.UserSessionRequest
import ir.faj.jalas.jalas.entities.Session
import ir.faj.jalas.jalas.service.user.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1.0/user")
class UserController(private val userService: UserService) {

    @GetMapping("/sessions")
    fun getUserSessions(@RequestBody request: UserSessionRequest): List<Session> {
        return userService.getUserSession(request.username)
    }
}