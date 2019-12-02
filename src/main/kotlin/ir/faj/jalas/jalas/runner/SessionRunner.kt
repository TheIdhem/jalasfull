package ir.faj.jalas.jalas.runner

import ir.faj.jalas.jalas.entities.Session
import ir.faj.jalas.jalas.entities.User
import ir.faj.jalas.jalas.entities.repository.SessionRepository
import ir.faj.jalas.jalas.entities.repository.UserRepository
import ir.faj.jalas.jalas.enums.SessionStatus
import ir.faj.jalas.jalas.utility.addDays
import ir.faj.jalas.jalas.utility.addHours
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.util.*

@Component
class SessionRunner(
        val sessions: SessionRepository,
        val users: UserRepository
) : CommandLineRunner {

    override fun run(vararg args: String?) {
//        if ((1..12).shuffled().first() % 2 == 1) {
//            users.deleteAll()
//            sessions.deleteAll()
//        }
//        val mehdi = users.save(User(username = "the_idhem1", email = "mohammadf00900@gmail.com", name = "mehdi"))
//        val golnaz = users.save(User(username = "grrinova1", email = "golnaz.a108@gmail.com", name = "golnaz"))
//        sessions.save(Session(title = "ست کردن اسکرام", owner = mehdi, endAt = Date().addHours(4), status = SessionStatus.successReserved, roomId = 800))
//        sessions.save(Session(title = "2ست کردن اسکرام", owner = golnaz, endAt = Date().addDays(4).addHours(4), status = SessionStatus.successReserved, roomId = 804))
//        sessions.save(Session(title = "2ست کردن اسکرام", owner = golnaz, endAt = Date().addDays(4).addHours(4), status = SessionStatus.successReserved, roomId = 804))
    }
}
