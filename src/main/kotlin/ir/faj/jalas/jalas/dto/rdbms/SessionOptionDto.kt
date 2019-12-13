package ir.faj.jalas.jalas.dto.rdbms

import ir.faj.jalas.jalas.entities.Session
import ir.faj.jalas.jalas.entities.SessionOption
import ir.faj.jalas.jalas.entities.User
import ir.faj.jalas.jalas.entities.Vote
import java.util.*

open class SessionOptionBaseDto : JalasDto<SessionOption> {
    var id: Int = 0
    var startAt: Date = Date()
    var endAt: Date = Date()
    var session: Session? = null
    var votes: List<Vote> = listOf()

    override fun toEntity(entity: SessionOption?): SessionOption {
        var model = entity ?: SessionOption()

        model.startAt = startAt
        model.endAt = endAt
        model.session = session ?: model.session
        model.votes = votes
        return model
    }
}

data class SessionOptionShallowDto(var rooms: List<Int> = listOf()) : SessionOptionBaseDto()