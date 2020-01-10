package ir.faj.jalas.jalas.service.session

import ir.faj.jalas.jalas.clients.model.AvailableRoomResponse
import ir.faj.jalas.jalas.controllers.model.*
import ir.faj.jalas.jalas.dto.rdbms.SessionOptionShallowDto
import ir.faj.jalas.jalas.dto.rdbms.SessionShallowDto
import ir.faj.jalas.jalas.entities.Session
import ir.faj.jalas.jalas.entities.User
import java.util.*

interface SessionService {
    fun getAvailableRoom(startAt: Date, endAt: Date): AvailableRoomResponse
    fun getAvrageTimeSession(): ReportResponse
    fun reserveRoom(reservationRequest: ReservationRequest, roomId: Int): Session
    fun reservSession(session: Session, reservationRequest: ReservationRequest, eventShouldLog: Boolean = false): Session
    fun notifySuccessReservation(user: User, session: Session)
    fun createSession(request: SessionRequest, user: User): SessionShallowDto
    fun editSession(request: SessionRequest, user: User): SessionShallowDto
    fun getAllSession(user: User): List<SessionShallowDto>
    fun getSessionWithId(user: User, sessionId: Int): SessionShallowDto
    fun voteToOption(request: SingleVoteRequest, user: User)
    fun deleteOption(optionId: Int, user: User):  List<SessionOptionShallowDto>
    fun changeSessionStatus(request: SessionStatusRequest, user: User)
}