package ir.faj.jalas.jalas.entities

import ir.faj.jalas.jalas.enums.SessionStatus
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import org.hibernate.annotations.Type
import java.util.*
import javax.persistence.*


@Entity
@Table(name = "ja_session")
@DynamicUpdate
class Session(
        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var Id: Int = 0,

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

        @Basic
        @Column(name = "title")
        var title: String = "",

        @Basic
        @Column(name = "status")
        @Type(type = "ir.faj.jalas.jalas.enums.SessionStatusClassTypeEnum")
        var status: SessionStatus = SessionStatus.pending,

        @Basic
        @Column(name = "room_id")
        var roomId: Int = 0,

        @OneToOne
        @JoinColumn(name = "owner_id", nullable = false)
        var owner: User = User(),

        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(
                name = "ja_session_user",
                joinColumns = [JoinColumn(name = "session_id")],
                inverseJoinColumns = [JoinColumn(name = "user_id")]
        )
        @Fetch(value = FetchMode.SELECT)
        var users: List<User> = mutableListOf(),

        @Basic
        @Column(name = "time_of_creation")
        var timeOfCreation: Int = 0

)

