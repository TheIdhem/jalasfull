package ir.faj.jalas.jalas.dto.rdbms

import ir.faj.jalas.jalas.entities.Vote
import ir.faj.jalas.jalas.enums.VoteType

open class VoteBaseDto : JalasDto<Vote> {
    var id: Int = 0
    var option: SessionOptionShallowDto? = null
    var user: UserShallowDto? = null
    var status: VoteType = VoteType.down

    override fun toEntity(entity: Vote?): Vote {
        val model = Vote()
        model.option = option?.toEntity() ?: model.option
        model.user = user?.toEntity() ?: model.user
        model.status = status
        return model
    }


}

class VoteShallowDto : VoteBaseDto()