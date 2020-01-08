package ir.faj.jalas.jalas.service.comment

import ir.faj.jalas.jalas.controllers.model.CommentRequest
import ir.faj.jalas.jalas.dto.rdbms.CommentShallowDto
import ir.faj.jalas.jalas.dto.rdbms.UserShallowDto

interface CommentService {
    fun addComment(comment: CommentRequest, user: UserShallowDto): CommentShallowDto
    fun deleteComment(user:UserShallowDto,commentId: Int):String
}