package ir.faj.jalas.jalas.service.comment

import ir.faj.jalas.jalas.controllers.model.CommentRequest
import ir.faj.jalas.jalas.dto.rdbms.CommentShallowDto
import ir.faj.jalas.jalas.dto.rdbms.UserShallowDto
import ir.faj.jalas.jalas.entities.Comment
import ir.faj.jalas.jalas.entities.repository.CommentRepository
import ir.faj.jalas.jalas.entities.repository.SessionRepository
import ir.faj.jalas.jalas.exception.NotAllowToDeleteComment
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
open class CommentServiceImpl(private val comments: CommentRepository,
                              private val sessions: SessionRepository) : CommentService {

    override fun addComment(request: CommentRequest, user: UserShallowDto): List<CommentShallowDto> {
        val session = sessions.findById(request.sessionId).get()
        val parentComment = request.parentCommentId?.let {
            comments.findById(it).get()
        }
        val sender = user.toEntity()
        val comment = Comment(sender = sender,
                session = session,
                content = request.content,
                parentComment = parentComment
        )
        comments.save(comment)
        return comments.findBySessionId(session.id)
                .filter { it.parentComment == null }
                .map { it.toShallow(true) }
    }

    @Transactional
    override fun deleteComment(user: UserShallowDto, commentId: Int): String {
        comments.findById(commentId).get().let { comment ->
            if (user.id == comment.sender.id || user.id == comment.session.owner.id)
                comments.deleteCommentById(comment.id)
            else throw NotAllowToDeleteComment()
        }
        return "Ok"
    }

}