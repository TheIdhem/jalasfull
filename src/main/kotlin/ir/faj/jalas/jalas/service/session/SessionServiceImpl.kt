package ir.faj.jalas.jalas.service.session

import feign.FeignException
import ir.faj.jalas.jalas.clients.JalasReservation
import ir.faj.jalas.jalas.clients.model.AvailableRoomResponse
import ir.faj.jalas.jalas.controllers.model.ReportResponse
import ir.faj.jalas.jalas.controllers.model.ReservationRequest
import ir.faj.jalas.jalas.controllers.model.SessionRequest
import ir.faj.jalas.jalas.entities.EventLog
import ir.faj.jalas.jalas.entities.Session
import ir.faj.jalas.jalas.entities.User
import ir.faj.jalas.jalas.entities.repository.EventLogRepository
import ir.faj.jalas.jalas.entities.repository.SessionRepository
import ir.faj.jalas.jalas.entities.repository.UserRepository
import ir.faj.jalas.jalas.enums.EventLogType
import ir.faj.jalas.jalas.enums.SessionStatus
import ir.faj.jalas.jalas.exception.*
import ir.faj.jalas.jalas.utility.GmailSender
import ir.faj.jalas.jalas.utility.toRoomServiceFormat
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.Exception
import java.lang.RuntimeException
import java.util.*


@Service
class SessionServiceImpl(val jalasReservation: JalasReservation,
                         val sessions: SessionRepository,
                         val events: EventLogRepository,
                         val users: UserRepository,
                         val gmailSender: GmailSender
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

    @Transactional
    private fun logEvent(session: Session, sessionStatus: SessionStatus, logStatus: EventLogType) {
        session.status = sessionStatus
        sessions.save(session)
        events.save(EventLog(eventType = logStatus, session = session))
    }

    override fun reservSession(session: Session, reservationRequest: ReservationRequest, eventShouldLog: Boolean): Session {
        return try {
            val startTime = Date()
            jalasReservation.reserveRoom(session.roomId, reservationRequest.of())
            session.status = SessionStatus.successReserved
            session.users.forEach {
                notifySuccessReservation(it, session)
            }
            notifySuccessReservation(session.owner, session)
            session.timeOfCreation = (Date().time - startTime.time).toInt()
            sessions.save(session)

        } catch (ex: FeignException) {
            logger.error("got exception in reservation ex:{${ex.status()}}")
            when (ex.status()) {
                400 -> {
                    if (eventShouldLog)
                        logEvent(session, SessionStatus.unavailble, EventLogType.roomNotAvailable)
                    throw RoomNotAvailable()
                }
                401 -> throw NotFoundRoom()
                500 -> {
                    logEvent(session, SessionStatus.pending, EventLogType.shouldRequestAgain)
                    throw InternalServerError()
                }
                else -> {
                    if (eventShouldLog)
                        logEvent(session, SessionStatus.unavailble, EventLogType.roomNotAvailable)
                    throw RoomNotAvailable()
                }
            }
        } catch (ex: Exception) {
            logger.warn("error not on request to reservation system ${ex.cause}")
            throw RuntimeException()
        }
    }

    override fun notifySuccessReservation(user: User, session: Session) {
        gmailSender.sendMail(
                subject = "Meeting Reservation Successfull",
                message = """
                            |Dear ${session.owner.name},
                            |
                            |Your meeting '${session.title}' at time [${session.startAt}, ${session.endAt}] has been successfully reserved at room ${session.roomId}.
                            |
                            |Best Regards,
                            |HamoonHamishegi Team
                        """.trimMargin(),
                to = user.email
        )
    }

    override fun createSession(request: SessionRequest): Session {
        val usersToSessions = request.users.map {
            it.createOrFindUser()
        }
        return sessions.save(Session(
                users = usersToSessions,
                title = request.title,
                owner = users.findByUsername(request.username),
                options = request.options.map { it.toEntity() }
        ))
    }

    override fun getAllSession(username: String): List<Session> {
        return users.findByUsername(username).sessions
    }

    private fun String.createOrFindUser(): User {
        return users.findByEmail(this) ?: users.save(User(email = this, username = this, name = this))
    }

    override fun getAvrageTimeSession(): ReportResponse {
        val sessionsReserved = sessions.findAllByStatus(SessionStatus.successReserved)
        val sessionCancled = sessions.findAllByStatus(SessionStatus.cancled)


        val averageTimeOfCreateSession = sessionsReserved.sumBy { it.timeOfCreation } / sessionsReserved.size.toDouble()
        return ReportResponse(averageTimeOfCreateSession = averageTimeOfCreateSession / 1000,
                numberOfSessionReserved = sessionsReserved.size,
                numberOfSessionCancled = sessionCancled.size)
    }

}


