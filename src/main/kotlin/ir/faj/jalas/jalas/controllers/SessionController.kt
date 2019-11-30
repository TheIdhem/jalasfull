package ir.faj.jalas.jalas.controllers

import ir.faj.jalas.jalas.clients.model.AvailableRoomResponse
import ir.faj.jalas.jalas.controllers.model.AvailableRoomRequest
import ir.faj.jalas.jalas.service.session.SessionService
import ir.faj.jalas.jalas.utility.endOfDay
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1.0")
class SessionController(val sessionService: SessionService) {

    @PostMapping("/session/room")
    fun getSession(@RequestBody request: AvailableRoomRequest): AvailableRoomResponse {
        return sessionService.getAvailableRoom(request.startAt, request.endAt)
    }
}