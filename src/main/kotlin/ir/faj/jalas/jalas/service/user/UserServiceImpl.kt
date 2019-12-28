package ir.faj.jalas.jalas.service.user

import ir.faj.jalas.jalas.controllers.model.UserRegisterRequest
import ir.faj.jalas.jalas.dto.rdbms.UserShallowDto
import ir.faj.jalas.jalas.entities.Session
import ir.faj.jalas.jalas.entities.User
import ir.faj.jalas.jalas.entities.repository.EventLogRepository
import ir.faj.jalas.jalas.entities.repository.SessionRepository
import ir.faj.jalas.jalas.entities.repository.UserRepository
import ir.faj.jalas.jalas.exception.NotFoundUser
import ir.faj.jalas.jalas.exception.UsernameAlreadyReserved
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.security.Principal

@Service
class UserServiceImpl(val users: UserRepository,
                      val passswordEncoder: PasswordEncoder

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

    override fun getUserInfo(username: String):UserShallowDto{
        return users.findByUsername(username)?.toShallow() ?: throw NotFoundUser()
    }
}