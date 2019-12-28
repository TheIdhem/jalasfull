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
    var session: SessionShallowDto? = null
    var votes: List<VoteShallowDto>? = listOf()
    var roomsCouldBeReserved: List<Int>? = null
    var agreeVotes: Int = 0
    var disAgreeVotes: Int = 0

    override fun toEntity(entity: SessionOption?): SessionOption {
        var model = entity ?: SessionOption()

        model.startAt = startAt
        model.endAt = endAt
        model.session = session?.toEntity()
        model.votes = votes?.map { it.toEntity() }
        return model
    }
}

class SessionOptionShallowDto(var rooms: List<Int> = listOf()) : SessionOptionBaseDto()