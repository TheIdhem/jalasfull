package ir.faj.jalas.jalas.service.user

import ir.faj.jalas.jalas.controllers.model.NotificationRequest
import ir.faj.jalas.jalas.controllers.model.UserRegisterRequest
import ir.faj.jalas.jalas.dto.rdbms.UserShallowDto
import ir.faj.jalas.jalas.entities.Notification
import ir.faj.jalas.jalas.entities.Session
import ir.faj.jalas.jalas.entities.User
import ir.faj.jalas.jalas.entities.repository.NotificationRepository
import ir.faj.jalas.jalas.entities.repository.SessionRepository
import ir.faj.jalas.jalas.entities.repository.UserRepository
import ir.faj.jalas.jalas.enums.NotificationType
import ir.faj.jalas.jalas.exception.CouldNotDeleteOwner
import ir.faj.jalas.jalas.exception.NotFoundUser
import ir.faj.jalas.jalas.exception.UserNotAllowToChange
import ir.faj.jalas.jalas.exception.UsernameAlreadyReserved
import ir.faj.jalas.jalas.utility.GmailSender
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.security.Principal

@Service
open class UserServiceImpl(val users: UserRepository,
                           val passswordEncoder: PasswordEncoder,
                           val sessions: SessionRepository,
                           val gmailSender: GmailSender,
                           val notifications: NotificationRepository

) : UserService {

    override fun getUserSession(username: String): List<Session> {
        return users.findByUsername(username)?.sessions ?: throw NotFoundUser()
    }

    override fun register(request: UserRegisterRequest): UserShallowDto {
        if (users.findAll().isNotEmpty())
            users.findByUsername(request.username)?.let { throw UsernameAlreadyReserved() }
        return users.save(User(name = request.username, password = passswordEncoder.encode(request.password), email = request.email, username = request.username)).toShallow()
    }

    override fun findByUserName(principal: Principal): User {
        return users.findByUsername(principal.name) ?: throw NotFoundUser()
    }

    override fun getUserInfo(username: String): UserShallowDto {
        return users.findByUsername(username)?.toShallow() ?: throw NotFoundUser()
    }

    override fun deleteUserFromSession(userId: Int, sessionId: Int, user: User): List<UserShallowDto> {
        val session = sessions.findById(sessionId).get()
        if (session.owner.id != user.id)
            throw UserNotAllowToChange()
        if (session.owner.id == userId)
            throw CouldNotDeleteOwner()
        session.users = session.users.mapNotNull {
            if (it.id == userId) {
                notifyForRemoveFromPoll(it, session.title)
                null
            } else it
        }
        sessions.save(session)
        return session.users.map { it.toShallow(true) }
    }

    private fun notifyForRemoveFromPoll(user: User, title: String) {
        if (notifications.findByUserIdAndType(user.id, NotificationType.addOrRemoveUser) == null)
            gmailSender.sendMail(
                    subject = "deleted from poll with title : $title",
                    message = """
                            |Dear ${user.name},
                            |
                            |you were deleted from poll with title $title.
                            |
                            |Best Regards,
                            |HamoonHamishegi Team
                        """.trimMargin(),
                    to = user.email
            )
    }

    @Transactional
    override fun addNotification(request: NotificationRequest, user: User) {
        notifications.findByUserIdAndType(user.id, request.type).let {
            if (it == null)
                notifications.save(Notification(user = user, type = request.type))
            else notifications.deleteByUserIdAndType(user.id, request.type)
        }
    }

}