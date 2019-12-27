package ir.faj.jalas.jalas.controllers

import ir.faj.jalas.jalas.controllers.model.UserRegisterRequest
import ir.faj.jalas.jalas.controllers.model.UserSessionRequest
import ir.faj.jalas.jalas.dto.rdbms.UserShallowDto
import ir.faj.jalas.jalas.entities.Session
import ir.faj.jalas.jalas.service.user.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1.0/user")
@CrossOrigin
class UserController(private val userService: UserService) {

    @GetMapping("/sessions")
    fun getUserSessions(@RequestBody request: UserSessionRequest): List<Session> {
        return userService.getUserSession(request.username)
    }

    @PostMapping("/register")
    fun getUserSessions(@RequestBody request: UserRegisterRequest): UserShallowDto {
        return userService.register(request)
    }
}