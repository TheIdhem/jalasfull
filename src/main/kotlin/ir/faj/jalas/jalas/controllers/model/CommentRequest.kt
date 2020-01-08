package ir.faj.jalas.jalas.controllers.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class CommentRequest(val content: String, val sessionId: Int, val parentCommentId: Int?)