package ir.faj.jalas.jalas.controllers

import ir.faj.jalas.jalas.clients.model.AvailableRoomResponse
import ir.faj.jalas.jalas.clients.model.RoomReservationRequest
import ir.faj.jalas.jalas.clients.model.RoomReservationResponse
import ir.faj.jalas.jalas.controllers.model.AvailableRoomRequest
import ir.faj.jalas.jalas.controllers.model.ReservationRequest
import ir.faj.jalas.jalas.controllers.model.SessionRequest
import ir.faj.jalas.jalas.entities.Session
import ir.faj.jalas.jalas.service.session.SessionService
import ir.faj.jalas.jalas.utility.endOfDay
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1.0/session")
class SessionController(val sessionService: SessionService) {


    @PostMapping
    fun createSession(@RequestBody request: SessionRequest):Session {
        return sessionService.createSession(request)
    }

    @PutMapping
    fun editSession(@RequestBody request: SessionRequest):Session {
        return sessionService.editSession(request)
    }

    @GetMapping
    fun getAllSessions(@RequestParam username:String) : List<Session> {
        return sessionService.getAllSession(username)
    }

    @PostMapping("/room")
    fun getSession(@RequestBody request: AvailableRoomRequest): AvailableRoomResponse {
        return sessionService.getAvailableRoom(request.startAt, request.endAt)
    }


    @PostMapping("/rooms/{roomId}/reserve")
    fun reservRoom(@RequestBody request: ReservationRequest, @PathVariable roomId:Int): Session {
        return sessionService.reservRoom(request,roomId)
    }
}