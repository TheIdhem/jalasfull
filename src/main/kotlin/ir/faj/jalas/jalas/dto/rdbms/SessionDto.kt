package ir.faj.jalas.jalas.dto.rdbms

import ir.faj.jalas.jalas.entities.Session
import ir.faj.jalas.jalas.enums.SessionStatus
import java.util.*


open class SessionBaseDto : JalasDto<Session> {
    var id: Int = 0
    var startAt: Date = Date()
    var endAt: Date = Date()
    var title: String = ""
    var status: SessionStatus = SessionStatus.pending
    var roomId: Int = 0
    var owner: UserShallowDto? = null
    var users: List<UserShallowDto>? = null
    var timeOfCreation: Int = 0
    var options: List<SessionOptionShallowDto>? = null

    override fun toEntity(entity: Session?): Session {
        val model = Session()
        model.id = id
        model.startAt = startAt
        model.endAt = endAt
        model.title = title
        model.status = status
        model.owner = owner?.toEntity() ?: model.owner
        model.users = users?.map { it.toEntity() } ?: model.users
        model.timeOfCreation = timeOfCreation
        model.options = options?.map { it.toEntity() }
        return model
    }


}

class SessionShallowDto : SessionBaseDto()