package ir.faj.jalas.jalas.service.session

import feign.FeignException
import ir.faj.jalas.jalas.clients.JalasReservation
import ir.faj.jalas.jalas.clients.model.AvailableRoomResponse
import ir.faj.jalas.jalas.clients.model.RoomReservationRequest
import ir.faj.jalas.jalas.clients.model.RoomReservationResponse
import ir.faj.jalas.jalas.controllers.model.ReservationRequest
import ir.faj.jalas.jalas.entities.EventLog
import ir.faj.jalas.jalas.entities.Session
import ir.faj.jalas.jalas.entities.repository.EventLogRepository
import ir.faj.jalas.jalas.entities.repository.SessionRepository
import ir.faj.jalas.jalas.entities.repository.UserRepository
import ir.faj.jalas.jalas.enums.EventLogType
import ir.faj.jalas.jalas.enums.SessionStatus
import ir.faj.jalas.jalas.exception.*
import ir.faj.jalas.jalas.utility.toRoomServiceFormat
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.ExceptionHandler
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.Locale


@Service
class SessionServiceImpl(val jalasReservation: JalasReservation,
                         val sessions: SessionRepository,
                         val events: EventLogRepository,
                         val users: UserRepository
) : SessionService {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    override fun getAvailableRoom(startAt: Date, endAt: Date): AvailableRoomResponse {
        return try {
            jalasReservation.getAvailableRoom(startAt.toRoomServiceFormat(), endAt.toRoomServiceFormat())
        } catch (ex: FeignException) {
            logger.error("error in get rooms from jalas service room : ex:{${ex.status()}}")
            when (ex.status()) {
                400 -> throw BadRequest()
                500 -> throw InternalServerErr()
            }
            AvailableRoomResponse()
        }

    }

    override fun reservRoom(reservationRequest: ReservationRequest, roomId: Int): Session {
        val user = users.findByUsername(reservationRequest.username)
        var session = Session(
                startAt = reservationRequest.startAt,
                endAt = reservationRequest.endAt,
                owner = user,
                roomId = roomId
        )
        return reservSession(session, reservationRequest)

    }

    private fun logEvent(session: Session, sessionStatus: SessionStatus, logStatus: EventLogType) {
        session.status = sessionStatus
        sessions.save(session)
        events.save(EventLog(eventType = logStatus, session = session))
    }

    override fun reservSession(session: Session, reservationRequest: ReservationRequest): Session {
        return try {
            jalasReservation.reserveRoom(session.roomId, reservationRequest.of())
            session.status = SessionStatus.successReserved
            sessions.save(session)


        } catch (ex: FeignException) {
            logger.error("got exception in reservation ex:{${ex.status()}}")
            when (ex.status()) {
                400 -> throw RoomNotAvailable()
                401 -> throw NotFoundRoom()
                500 -> {
                    logEvent(session, SessionStatus.pending, EventLogType.shouldRequestAgain)
                    throw InternalServerError()
                }
                else -> {
                    logEvent(session, SessionStatus.unavailble, EventLogType.shouldRequestAgain)
                    throw RoomNotAvailable()
                }
            }
        }
    }

}


