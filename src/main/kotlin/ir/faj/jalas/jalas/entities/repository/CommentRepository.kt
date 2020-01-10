package ir.faj.jalas.jalas.entities.repository

import ir.faj.jalas.jalas.entities.Comment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface CommentRepository : JpaRepository<Comment, Int>, JpaSpecificationExecutor<Comment> {
    @Modifying
    @Query("delete from Comment c where c.id =?1")
    fun deleteCommentById(commentId: Int)

    fun findBySessionId(sessionId: Int): List<Comment>
}