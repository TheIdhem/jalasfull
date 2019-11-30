package ir.faj.jalas.jalas.service.session

import ir.faj.jalas.jalas.clients.model.AvailableRoomResponse
import ir.faj.jalas.jalas.clients.model.RoomReservationResponse
import ir.faj.jalas.jalas.controllers.model.ReservationRequest
import java.util.*

interface SessionService {
    fun getAvailableRoom(startAt: Date, endAt: Date): AvailableRoomResponse
    fun reservRoom(reservationRequest: ReservationRequest, roomId: Int): RoomReservationResponse
}