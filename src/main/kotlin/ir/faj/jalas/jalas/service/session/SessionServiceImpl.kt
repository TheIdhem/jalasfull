package ir.faj.jalas.jalas.service.session

import feign.FeignException
import ir.faj.jalas.jalas.clients.JalasReservation
import ir.faj.jalas.jalas.clients.model.AvailableRoomResponse
import ir.faj.jalas.jalas.controllers.model.*
import ir.faj.jalas.jalas.dto.rdbms.SessionOptionShallowDto
import ir.faj.jalas.jalas.dto.rdbms.SessionShallowDto
import ir.faj.jalas.jalas.entities.*
import ir.faj.jalas.jalas.entities.repository.*
import ir.faj.jalas.jalas.enums.EventLogType
import ir.faj.jalas.jalas.enums.NotificationType
import ir.faj.jalas.jalas.enums.SessionStatus
import ir.faj.jalas.jalas.enums.VoteType
import ir.faj.jalas.jalas.exception.*
import ir.faj.jalas.jalas.utility.GmailSender
import ir.faj.jalas.jalas.utility.toRoomServiceFormat
import org.slf4j.LoggerFactory
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
                              val passwordEncoder: PasswordEncoder,
                              val notifications: NotificationRepository
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

    override fun reserveRoom(reservationRequest: ReservationRequest, roomId: Int): SessionShallowDto {
        reservationRequest.username ?: throw NotFoundUser()
        val session = sessions.findById(reservationRequest.sessionId).get()
        val option = options.findById(reservationRequest.optionId).get()
        reservationRequest.startAt = option.startAt
        reservationRequest.endAt = option.endAt
        session.roomId = roomId
        return reservSession(session, reservationRequest).toShallow(true)

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
            session.startAt = reservationRequest.startAt!!
            session.endAt = reservationRequest.endAt!!
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
                    eventShouldLog(session, eventShouldLog)
                    throw RoomNotAvailable()
                }
                401 -> throw NotFoundRoom()
                500 -> {
                    logEvent(session, SessionStatus.pending, EventLogType.shouldRequestAgain)
                    throw InternalServerError()
                }
                503 -> {
                    logEvent(session, SessionStatus.pending, EventLogType.shouldRequestAgain)
                    throw InternalServerError()
                }
                else -> {
                    eventShouldLog(session, eventShouldLog)
                    throw RoomNotAvailable()
                }
            }
        } catch (ex: Exception) {
            logger.warn("error not on request to reservation system ${ex.cause}")
            throw RuntimeException()
        }
    }

    private fun eventShouldLog(session: Session, eventShouldLog: Boolean) {
        if (eventShouldLog)
            logEvent(session, SessionStatus.unavailble, EventLogType.roomNotAvailable)
        throw RoomNotAvailable()
    }

    override fun notifySuccessReservation(user: User, session: Session) {
        if (notifications.findByUserIdAndType(user.id, NotificationType.reservationSession) == null)
            gmailSender.sendMail(
                    subject = "Meeting Reservation Successfull",
                    message = """
                            |Dear ${user.name},
                            |
                            |Your meeting '${session.title}' at time [${session.startAt}, ${session.endAt}] has been successfully reserved at room ${session.roomId}.
                            |this is the link to see your session data http://localhost:3000/session/${session.id}
                            |
                            |Best Regards,
                            |HamoonHamishegi Team
                        """.trimMargin(),
                    to = user.email
            )
    }

    private fun notifyForVoteToPoll(session: Session, voter: User, option: SessionOption) {
        if (notifications.findByUserIdAndType(session.owner.id, NotificationType.voteToOption) == null)
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

    private fun notifyForDeletePoll(user: User, option: SessionOption) {
        if (notifications.findByUserIdAndType(user.id, NotificationType.deletePoll) == null)
            gmailSender.sendMail(
                    subject = "deleted option",
                    message = """
                            |Dear ${user.name},
                            |
                            |the option of the poll where deleted by owner of session and the time of option is[${option.startAt}, ${option.endAt}].
                            |
                            |Best Regards,
                            |HamoonHamishegi Team
                        """.trimMargin(),
                    to = user.email
            )
    }

    private fun notifyForAddAdditionalOption(title: String, user: User, option: SessionOption) {
        if (notifications.findByUserIdAndType(user.id, NotificationType.addOrRemoveOption) == null)
            gmailSender.sendMail(
                    subject = "add new option to session with title : $title",
                    message = """
                            |Dear ${user.name},
                            |
                            |'owner of poll added new option and the time of option is[${option.startAt}, ${option.endAt}].
                            |
                            |Best Regards,
                            |HamoonHamishegi Team
                        """.trimMargin(),
                    to = user.email
            )
    }

    private fun notifyAddedToPoll(user: User, session: Session) {
        if (notifications.findByUserIdAndType(user.id, NotificationType.addOrRemoveToPoll) == null)
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

        var session = Session(
                users = usersToSessions + user.email.createOrFindUser(),
                title = request.title,
                owner = user
        )
        usersToSessions.forEach {
            notifyAddedToPoll(it, session)
        }

        session = sessions.save(session)


        request.options.forEach {
            Pair(it.startAt, it.endAt).createOrFindOptions(it.id, session).first
        }

        return session.toShallow()
    }

    @Transactional
    override fun editSession(request: SessionRequest, user: User): SessionShallowDto {
        val session = sessions.findById(request.sessionId).get()

        if (session.owner.id != user.id)
            throw UserNotAllowToChange()

        val sessionsMapUser = session.users.associateBy { it.id }

        session.users = request.users.map {
            val user = it.createOrFindUser()
            if (sessionsMapUser[user.id] == null)
                notifyAddedToPoll(user, session)
            user
        }
        session.title = request.title

        session.options = request.options.map {
            val (option, isNew) = Pair(it.startAt, it.endAt).createOrFindOptions(it.id, session)
            if (isNew)
                session.users.emailForAddNewOption(option, session.title)
            option
        }

        return sessions.save(session).toShallow()

    }

    private fun List<User>.emailForAddNewOption(option: SessionOption, title: String) {
        this.forEach {
            notifyForAddAdditionalOption(title, it, option)
        }
    }

    override fun getAllSession(user: User): List<SessionShallowDto> {
        return user.sessions
                ?.map {
                    it.comments = it.comments?.filter { it.parentComment == null }
                    it
                }?.map { it.toShallow() }?.map {
                    it.options?.forEach { option ->
                        try {
                            option.agreeVotes = option.votes?.filter { it.status == VoteType.up }?.size ?: 0
                            option.disAgreeVotes = option.votes?.filter { it.status == VoteType.down }?.size ?: 0
                            option.sosoVotes = option.votes?.filter { it.status == VoteType.soso }?.size ?: 0
                        } catch (ex: Exception) {
                            logger.info("get session got an exception ${option.id}")
                        }
                    }
                    it
                } ?: listOf()
    }

    override fun getSessionWithId(user: User, sessionId: Int): SessionShallowDto {
        val session = sessions.findById(sessionId).get().toShallow()
        session.users?.find { user.id == it.id } ?: throw NotAllowToAccess()
        session.options?.forEach { option ->
            try {
                option.roomsCouldBeReserved = getAvailableRoom(option.startAt, option.endAt).availableRooms
            } catch (ex: Exception) {
                logger.warn("got exception to get rooms : $ex")
            }
        }
        return session
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

    @Transactional
    override fun deleteOption(optionId: Int, user: User): List<SessionOptionShallowDto> {
        val option = options.findById(optionId).get()
        if (option.session?.owner?.id != user.id)
            throw NotAllowToDeleteOption()
        option.votes?.map { it.user }?.emailToUserThatOptionWhereDeleted(option)
        options.deleteOptionById(optionId)
        return options.findBySessionId(option.session!!.id).map { it.toShallow(true) }
    }

    private fun String.createOrFindUser(): User {
        return users.findByEmail(this)
                ?: users.save(User(email = this, username = this, name = this, password = passwordEncoder.encode(this)))
    }

    private fun List<User>.emailToUserThatOptionWhereDeleted(option: SessionOption) {
        this.forEach {
            notifyForDeletePoll(it, option)
        }
    }


    open fun Pair<Date, Date>.createOrFindOptions(optionId: Int, session: Session): Pair<SessionOption, Boolean> {
        val option = options.findById(optionId)
        return Pair(options.save(if (option.isPresent) {
            val option = option.get()
            option.startAt = this.first
            option.endAt = this.second
            option
        } else SessionOption(startAt = this.first, endAt = this.second, session = session)), option.isPresent)
    }

    override fun getAvrageTimeSession(): ReportResponse {
        val sessionsReserved = sessions.findAllByStatus(SessionStatus.successReserved)
        val sessionCancled = sessions.findAllByStatus(SessionStatus.cancled)


        val averageTimeOfCreateSession = sessionsReserved.sumBy { it.timeOfCreation } / sessionsReserved.size.toDouble()
        return ReportResponse(averageTimeOfCreateSession = averageTimeOfCreateSession / 1000 * 10,
                numberOfSessionReserved = sessionsReserved.size,
                numberOfSessionCancled = sessionCancled.size)
    }

    @Transactional
    override fun changeSessionStatus(request: SessionStatusRequest, user: User) {
        val session = sessions.findById(request.sessionId).orElseThrow { throw NotFoundSession() }
        if (session.owner.id != user.id)
            throw UserNotAllowToChange()
        val pollOrSession = if (session.status == SessionStatus.successReserved) "session" else "poll"
        session.status = request.status
        session.users.forEach { user ->
            notifyForCanceledPollOrSession(user, session, pollOrSession)
        }
    }


    private fun notifyForCanceledPollOrSession(user: User, session: Session, pollOrSession: String) {
        if (notifications.findByUserIdAndType(user.id, NotificationType.reservationSession) == null)
            gmailSender.sendMail(
                    subject = "cancle $pollOrSession",
                    message = """
                            |Dear ${user.name},
                            |
                            |$pollOrSession with title of ${session.title} where cancled'.
                            |
                            |Best Regards,
                            |HamoonHamishegi Team
                        """.trimMargin(),
                    to = user.email
            )
    }

}


