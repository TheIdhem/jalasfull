package ir.faj.jalas.jalas.service.user

import ir.faj.jalas.jalas.entities.Session
import ir.faj.jalas.jalas.entities.repository.EventLogRepository
import ir.faj.jalas.jalas.entities.repository.SessionRepository
import ir.faj.jalas.jalas.entities.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(val sessions: SessionRepository,
                         val events: EventLogRepository,
                         val users: UserRepository

) : UserService {

    override fun getUserSession(username: String): List<Session> {
        return users.findByUsername(username).sessions
    }
}