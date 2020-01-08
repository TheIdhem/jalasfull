package ir.faj.jalas.jalas.entities

import ir.faj.jalas.jalas.dto.rdbms.CommentShallowDto
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "ja_comment")
@DynamicUpdate
class Comment(
        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int = 0,

        @Basic
        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "creation_time")
        var creationTime: Date = Date(),


        @Basic
        @Column(name = "content")
        var content: String = "",


        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "sender_id", nullable = false)
        var sender: User = User(),

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "session_id", nullable = false)
        var session: Session = Session(),

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "parent_comment_id", nullable = false)
        var parentComment: Comment? = null,


        @OneToMany(mappedBy = "parentComment", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
        @Fetch(value = FetchMode.SELECT)
        var replies: List<Comment>? = null

) {
    constructor() : this(id = 0)

    fun toShallow(isNested: Boolean = false): CommentShallowDto {
        val dto = CommentShallowDto()
        if (!isNested) {
            dto.parentComment = parentComment?.toShallow(true) ?: dto.parentComment
        }
        dto.replies = replies?.map { it.toShallow(true) } ?: dto.replies
        dto.content = content
        dto.sender = sender.toShallow(true)
        dto.session = session.toShallow(true)
        dto.id = id
        dto.creationTime = creationTime
        return dto
    }
}

