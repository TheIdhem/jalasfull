package ir.faj.jalas.jalas.dto.rdbms

import ir.faj.jalas.jalas.entities.User

open class UserBaseDto : JalasDto<User> {
    var id: Int = 0
    var name: String = ""
    var email: String = ""
    var username: String = ""
    var sessions: List<SessionShallowDto>? = null
    var votes: List<VoteShallowDto>? = null

    override fun toEntity(entity: User?): User {
        var model = User()
        model.name = name
        model.email = email
        model.username = username
        model.sessions = sessions?.map { it.toEntity() }
        model.votes = votes?.map { it.toEntity() }
        return model
    }

}

class UserShallowDto : UserBaseDto()