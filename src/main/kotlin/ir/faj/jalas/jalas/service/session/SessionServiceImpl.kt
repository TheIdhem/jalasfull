package ir.faj.jalas.jalas.service.session

import feign.FeignException
import ir.faj.jalas.jalas.clients.JalasReservation
import ir.faj.jalas.jalas.clients.model.AvailableRoomResponse
import ir.faj.jalas.jalas.controllers.model.*
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
import org.omg.CosNaming.NamingContextPackage.NotFound
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
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
                              val options: SessionOptionRepository,
                              val passwordEncoder: PasswordEncoder
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
        val username = reservationRequest.username ?: throw NotFoundUser()
        val user = users.findByUsername(username) ?: throw NotFoundUser()
        val session = sessions.findById(reservationRequest.sessionId).get()
        val option = options.findById(reservationRequest.optionId).get()
        reservationRequest.startAt = option.startAt
        reservationRequest.endAt = option.endAt
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

    private fun notifyForVoteToPoll(session: Session, voter: User, option: SessionOption) {
        gmailSender.sendMail(
                subject = "'${voter.name}' vote to poll",
                message = """
                            |Dear ${session.owner.name},
                            |
                            |'${voter.name}' vote to poll with title of '${session.title}' and the time of vote is[${option.startAt}, ${option.endAt}].
                            |
                            |Best Regards,
                            |HamoonHamishegi Team
                        """.trimMargin(),
                to = session.owner.email
        )
    }

    private fun notifyAddedToPoll(user: User, session: Session) {
        gmailSender.sendMail(
                subject = "Add to poll",
                message = """
                            |Dear ${user.name},
                            |
                            |You are added to poll with title '${session.title}'.
                            |
                            |Best Regards,
                            |HamoonHamishegi Team
                        """.trimMargin(),
                to = user.email
        )
    }

    override fun createSession(request: SessionRequest, user: User): SessionShallowDto {
        var usersToSessions = request.users.map {
            it.createOrFindUser()
        }


        val session = sessions.save(Session(
                users = usersToSessions + user.email.createOrFindUser(),
                title = request.title,
                owner = user
        ))

        usersToSessions.forEach {
            notifyAddedToPoll(it, session)
        }

        request.options.forEach {
            Pair(it.startAt, it.endAt).createOrFindOptions(it.id, session)
        }

        return session.toShallow()
    }

    @Transactional
    override fun editSession(request: SessionRequest, user: User): SessionShallowDto {
        val session = sessions.findById(request.sessionId).get()

        if (session.owner.id != user.id)
            throw UserNotAllowToChange()
        val sessionsMapUser = session.users.associateBy { it.id }

        request.users.forEach {

        }

        session.users = request.users.map {
            val user = it.createOrFindUser()
            if (sessionsMapUser[user.id] != null)
                notifyAddedToPoll(user, session)
            user
        }

        session.options = request.options.map {
            Pair(it.startAt, it.endAt).createOrFindOptions(it.id, session)
        }

        session.title = request.title
        return sessions.save(session).toShallow()

    }

    override fun getAllSession(user: User): List<SessionShallowDto> {
        return user.sessions?.map { it.toShallow() }?.map {
            it.options?.forEach { option ->
                try {
                    option.roomsCouldBeReserved = getAvailableRoom(option.startAt, option.endAt).availableRooms
                    option.agreeVotes = option.votes?.filter { it.status == VoteType.up }?.size ?: 0
                    option.disAgreeVotes = option.votes?.filter { it.status == VoteType.down }?.size ?: 0
                } catch (ex: Exception) {
                    val a = ex
                    //hisssss
                }
            }
            it
        } ?: listOf()
    }

    @Transactional
    override fun voteToOptions(request: VoteRequest, user: User) {
        if (votes.findByUserAndOptionIdIsIn(user, request.agreeOptionIds)?.isNotEmpty() == true)
            votes.deleteVotesByOptionIdAndUserId(request.agreeOptionIds, user.id)

        request.agreeOptionIds.forEach { optionId ->
            val option = options.findById(optionId).get()
            if (votes.findByUserAndOptionAndStatus(user, option, VoteType.up) == null)
                votes.save(Vote(option = option, user = user, status = VoteType.up))
        }
        request.disAgreeOptionIds?.forEach { optionId ->
            val option = options.findById(optionId).get()
            if (votes.findByUserAndOptionAndStatus(user, option, VoteType.down) == null)
                votes.save(Vote(option = option, user = user, status = VoteType.down))
        }
    }

    @Transactional
    override fun voteToOption(request: SingleVoteRequest, user: User) {
        if (request.status == VoteType.delete) {
            val vote = votes.findByUserAndOptionId(user, request.optionId) ?: throw NotFoundVote()
            votes.delete(vote)
        } else {

            val vote = votes.findByUserAndOptionId(user, request.optionId)?.let { vote ->
                vote.status = request.status
                votes.save(vote)
            }
                    ?: votes.save(Vote(status = request.status, option = options.findById(request.optionId).get(), user = user))
            if (user.id != vote.option.session?.owner?.id)
                notifyForVoteToPoll(vote.option.session!!, user, vote.option)
        }
    }

    private fun String.createOrFindUser(): User {
        return users.findByEmail(this)
                ?: users.save(User(email = this, username = this, name = this, password = passwordEncoder.encode(this)))
    }


    open fun Pair<Date, Date>.createOrFindOptions(optionId: Int, session: Session): SessionOption {
        val option = options.findById(optionId)
        return options.save(if (option.isPresent) {
            val option = option.get()
            option.startAt = this.first
            option.endAt = this.second
            option
        } else SessionOption(startAt = this.first, endAt = this.second, session = session))
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


