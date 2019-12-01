package ir.faj.jalas.jalas.service.session

import feign.FeignException
import ir.faj.jalas.jalas.clients.JalasReservation
import ir.faj.jalas.jalas.clients.model.AvailableRoomResponse
import ir.faj.jalas.jalas.clients.model.RoomReservationRequest
import ir.faj.jalas.jalas.clients.model.RoomReservationResponse
import ir.faj.jalas.jalas.controllers.model.ReservationRequest
import ir.faj.jalas.jalas.entities.Session
import ir.faj.jalas.jalas.entities.repository.EventLogRepository
import ir.faj.jalas.jalas.entities.repository.SessionRepository
import ir.faj.jalas.jalas.entities.repository.UserRepository
import ir.faj.jalas.jalas.enums.SessionStatus
import ir.faj.jalas.jalas.utility.toRoomServiceFormat
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.Locale


@Service
class SessionServiceImpl(val jalasReservation: JalasReservation,
                         val sessions: SessionRepository,
                         val events: EventLogRepository,
                         val users:UserRepository
) : SessionService {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    override fun getAvailableRoom(startAt: Date, endAt: Date): AvailableRoomResponse {
        return try {
            jalasReservation.getAvailableRoom(startAt.toRoomServiceFormat(), endAt.toRoomServiceFormat())
        } catch (ex: Exception) {
            logger.error("error in get rooms from jalas service room : ex:{${ex.cause}}")
            AvailableRoomResponse()
        }

    }

    override fun reservRoom(reservationRequest: ReservationRequest, roomId: Int): Session {
        val user = users.findByUsername(reservationRequest.username)
        return try {
            jalasReservation.reserveRoom(roomId, reservationRequest.of())
            sessions.save(Session(
                    startAt = reservationRequest.startAt,
                    endAt = reservationRequest.endAt,
                    status = SessionStatus.successReserved,
                    owner= user
            ))


        } catch (ex: FeignException) {
            logger.error("got exception in reservation ex:{${ex.status()}}")
            Session()
        }

    }

}


