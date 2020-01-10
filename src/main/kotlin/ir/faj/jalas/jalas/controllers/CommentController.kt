package ir.faj.jalas.jalas.controllers

import ir.faj.jalas.jalas.controllers.model.CommentRequest
import ir.faj.jalas.jalas.dto.rdbms.CommentShallowDto
import ir.faj.jalas.jalas.service.comment.CommentService
import ir.faj.jalas.jalas.service.user.UserService
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/v1.0/comment")
class CommentController(private val commentService: CommentService,
                        private val userService: UserService) {
    @PostMapping
    fun addComment(@RequestBody request: CommentRequest, principal: Principal): List<CommentShallowDto> {
        val user = userService.getUserInfo(principal.name)
        return commentService.addComment(request, user)
    }

    @DeleteMapping("/{commentId}")
    fun deleteComment(@PathVariable commentId: Int, principal: Principal):  List<CommentShallowDto> {
        val user = userService.getUserInfo(principal.name)
        return commentService.deleteComment(user, commentId)
    }

}