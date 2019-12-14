package ir.faj.jalas.jalas.service.session

import feign.FeignException
import ir.faj.jalas.jalas.clients.JalasReservation
import ir.faj.jalas.jalas.clients.model.AvailableRoomResponse
import ir.faj.jalas.jalas.controllers.model.ReportResponse
import ir.faj.jalas.jalas.controllers.model.ReservationRequest
import ir.faj.jalas.jalas.controllers.model.SessionRequest
import ir.faj.jalas.jalas.controllers.model.VoteRequest
import ir.faj.jalas.jalas.dto.rdbms.SessionShallowDto
import ir.faj.jalas.jalas.dto.rdbms.VoteShallowDto
import ir.faj.jalas.jalas.entities.*
import ir.faj.jalas.jalas.entities.repository.*
import ir.faj.jalas.jalas.enums.EventLogType
import ir.faj.jalas.jalas.enums.SessionStatus
import ir.faj.jalas.jalas.enums.VoteType
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
open class SessionServiceImpl(val jalasReservation: JalasReservation,
                              val sessions: SessionRepository,
                              val events: EventLogRepository,
                              val users: UserRepository,
                              val gmailSender: GmailSender,
                              val votes: VoteRepository,
                              val options: SessionOptionRepository
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

    override fun reserveRoom(reservationRequest: ReservationRequest, roomId: Int): Session {
        val user = users.findByUsername(reservationRequest.username) ?: throw NotFoundUser()
        var session = Session(
                startAt = reservationRequest.startAt,
                endAt = reservationRequest.endAt,
                owner = user,
                roomId = roomId
        )
        return reservSession(session, reservationRequest)

    }

    @Transactional
    open fun logEvent(session: Session, sessionStatus: SessionStatus, logStatus: EventLogType) {
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

    override fun createSession(request: SessionRequest): SessionShallowDto {
        val owner = users.findByUsername(request.username) ?: throw NotFoundUser()
        var usersToSessions = request.users.map {
            it.createOrFindUser()
        }
        val session = sessions.save(Session(
                users = usersToSessions + owner.email.createOrFindUser(),
                title = request.title,
                owner = owner
        ))
        request.options.forEach {
            Pair(it.startAt, it.endAt).createOrFindOptions(it.id, session, listOf())
        }
        return session.toShallow()
    }

    @Transactional
    override fun editSession(request: SessionRequest): SessionShallowDto {
        val session = sessions.findById(request.sessionId).get()
        votes.deleteVotesByOptionId(request.options.map { it.id })
        options.deleteOptionsById(request.options.map { it.id })
        session.users = request.users.map {
            it.createOrFindUser()
        } + session.owner
        session.options = request.options.map {
            Pair(it.startAt, it.endAt).createOrFindOptions(it.id, session, it.votes)
        }
        session.title = request.title
        return sessions.save(session).toShallow()

    }

    override fun getAllSession(username: String): List<SessionShallowDto> {
        return users.findByUsername(username)?.sessions?.map { it.toShallow() } ?: listOf()
    }

    override fun voteToOptions(request: VoteRequest) {
        val user = users.findByUsername(request.username) ?: throw NotFoundUser()
        request.agreeOptionIds.forEach { votes.save(Vote(option = options.findById(it).get(), user = user, status = VoteType.up)) }
        request.disAgreeOptionIds.forEach { votes.save(Vote(option = options.findById(it).get(), user = user, status = VoteType.down)) }
    }

    private fun String.createOrFindUser(): User {
        return users.findByEmail(this) ?: users.save(User(email = this, username = this, name = this))
    }

    private fun Pair<Date, Date>.createOrFindOptions(optionId: Int, session: Session, optionVotes: List<VoteShallowDto>?): SessionOption {
        val optionVotesSaved = optionVotes?.map { votes.save(it.toEntity()) }
        val option = options.findById(optionId)
        return if (option.isPresent) option.get() else options.save(SessionOption(startAt = this.first, endAt = this.second, session = session, votes = optionVotesSaved))
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


