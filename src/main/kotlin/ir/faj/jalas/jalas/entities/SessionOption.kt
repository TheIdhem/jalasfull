package ir.faj.jalas.jalas.entities

import ir.faj.jalas.jalas.dto.rdbms.SessionOptionShallowDto
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "ja_session_option")
@DynamicUpdate
class SessionOption(
        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int = 0,

        @Basic
        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "creation_time")
        var creationTime: Date = Date(),

        @Basic
        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "start_at")
        var startAt: Date = Date(),

        @Basic
        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "end_at")
        var endAt: Date = Date(),

        @OneToMany(mappedBy = "option", fetch = FetchType.EAGER)
        @Fetch(value = FetchMode.SELECT)
        var votes: List<Vote>? = listOf(),

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "session_id", nullable = false)
        var session: Session? = Session()


) {
    constructor() : this(id = 0)

    fun toShallow(isNested: Boolean = false): SessionOptionShallowDto {
        val dto = SessionOptionShallowDto()
        if (!isNested) {
            dto.session = session?.toShallow(true) ?: dto.session
        }
        dto.votes = votes?.map { it.toShallow(true) } ?: dto.votes

        dto.startAt = startAt
        dto.endAt = endAt
        dto.id = id
        return dto

    }
}

