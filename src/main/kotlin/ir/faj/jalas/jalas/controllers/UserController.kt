package ir.faj.jalas.jalas.controllers

import ir.faj.jalas.jalas.controllers.model.NotificationRequest
import ir.faj.jalas.jalas.controllers.model.UserRegisterRequest
import ir.faj.jalas.jalas.controllers.model.UserSessionRequest
import ir.faj.jalas.jalas.dto.rdbms.UserShallowDto
import ir.faj.jalas.jalas.entities.Session
import ir.faj.jalas.jalas.service.user.UserService
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/v1.0/user")
@CrossOrigin
class UserController(private val userService: UserService) {

    @GetMapping("/sessions")
    fun getUserSessions(@RequestBody request: UserSessionRequest): List<Session> {
        return userService.getUserSession(request.username)
    }

    @GetMapping("/info")
    fun getUserInfo(principal: Principal): UserShallowDto {
        return userService.getUserInfo(principal.name)
    }

    @PostMapping("/register")
    fun getUserSessions(@RequestBody request: UserRegisterRequest): UserShallowDto {
        return userService.register(request)
    }

    @DeleteMapping("{userId}/session/{sessionId}")
    fun deleteUserFromSession(@PathVariable userId: Int, @PathVariable sessionId: Int, principal: Principal) {
        val user = userService.findByUserName(principal)
        userService.deleteUserFromSession(userId, sessionId, user)
    }

    @PostMapping("/notification")
    fun addNotification(@RequestBody request: NotificationRequest, principal: Principal) {
        val user = userService.findByUserName(principal)
        userService.addNotification(request, user)
    }
}