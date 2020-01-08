package ir.faj.jalas.jalas.dto.rdbms

import ir.faj.jalas.jalas.entities.Comment
import java.util.*


open class CommentBaseDto : JalasDto<Comment> {
    var id: Int = 0
    var creationTime: Date = Date()
    var content: String = ""
    var replies: List<CommentShallowDto>? = null
    var parentComment: CommentShallowDto? = null
    var sender: UserShallowDto? = null
    var session: SessionShallowDto? = null

    override fun toEntity(entity: Comment?): Comment {
        val model = entity ?: Comment()
        model.content = content
        model.replies = replies?.map { it.toEntity() } ?: model.replies
        model.sender = sender?.toEntity() ?: model.sender
        model.parentComment = parentComment?.toEntity() ?: model.parentComment
        model.session = session?.toEntity() ?: model.session
        model.creationTime = creationTime
        return model
    }


}

class CommentShallowDto : CommentBaseDto()