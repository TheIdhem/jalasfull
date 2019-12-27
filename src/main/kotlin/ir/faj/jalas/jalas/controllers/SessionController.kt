package ir.faj.jalas.jalas.controllers

import ir.faj.jalas.jalas.clients.model.AvailableRoomResponse
import ir.faj.jalas.jalas.clients.model.RoomReservationRequest
import ir.faj.jalas.jalas.clients.model.RoomReservationResponse
import ir.faj.jalas.jalas.controllers.model.AvailableRoomRequest
import ir.faj.jalas.jalas.controllers.model.ReservationRequest
import ir.faj.jalas.jalas.controllers.model.SessionRequest
import ir.faj.jalas.jalas.controllers.model.VoteRequest
import ir.faj.jalas.jalas.dto.rdbms.SessionShallowDto
import ir.faj.jalas.jalas.entities.Session
import ir.faj.jalas.jalas.service.session.SessionService
import ir.faj.jalas.jalas.service.user.UserService
import ir.faj.jalas.jalas.utility.endOfDay
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*

@RestController
@RequestMapping("/api/v1.0/session")
class SessionController(val sessionService: SessionService, val userService: UserService) {


    @PostMapping
    fun createSession(@RequestBody request: SessionRequest, principal: Principal): SessionShallowDto {
        val user = userService.findByUserName(principal)
        return sessionService.createSession(request, user)
    }

    @PutMapping
    fun editSession(@RequestBody request: SessionRequest, principal: Principal): SessionShallowDto {
        val user = userService.findByUserName(principal)
        return sessionService.editSession(request, user)
    }

    @GetMapping
    fun getAllSessions(principal: Principal): List<SessionShallowDto> {
        val user = userService.findByUserName(principal)
        return sessionService.getAllSession(user)
    }

    @PostMapping("/room")
    fun getSession(@RequestBody request: AvailableRoomRequest): AvailableRoomResponse {
        return sessionService.getAvailableRoom(request.startAt, request.endAt)
    }


    @PostMapping("/rooms/{roomId}/reserve")
    fun reserveRoom(@RequestBody request: ReservationRequest, @PathVariable roomId: Int, principal: Principal): Session {
        request.username = userService.findByUserName(principal).username
        return sessionService.reserveRoom(request, roomId)
    }

    @PostMapping("/vote")
    fun voteToOptions(@RequestBody request: VoteRequest, principal: Principal): String {
        val user = userService.findByUserName(principal)
        sessionService.voteToOptions(request, user)
        return "OK"
    }

    @PostMapping("/{optionId}/vote")
    fun voteToOption(@PathVariable optionId: Int, principal: Principal): String {
        val user = userService.findByUserName(principal)
        sessionService.voteToOption(optionId, user)
        return "OK"
    }
}