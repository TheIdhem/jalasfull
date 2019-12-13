package ir.faj.jalas.jalas.service.session

import ir.faj.jalas.jalas.clients.model.AvailableRoomResponse
import ir.faj.jalas.jalas.controllers.model.ReportResponse
import ir.faj.jalas.jalas.controllers.model.ReservationRequest
import ir.faj.jalas.jalas.controllers.model.SessionRequest
import ir.faj.jalas.jalas.entities.Session
import ir.faj.jalas.jalas.entities.User
import java.util.*

interface SessionService {
    fun getAvailableRoom(startAt: Date, endAt: Date): AvailableRoomResponse
    fun getAvrageTimeSession(): ReportResponse
    fun reservRoom(reservationRequest: ReservationRequest, roomId: Int): Session
    fun reservSession(session: Session, reservationRequest: ReservationRequest, eventShouldLog: Boolean = false): Session
    fun notifySuccessReservation(user: User, session: Session)
    fun createSession(request:SessionRequest):Session
    fun getAllSession(username: String): List<Session>
}