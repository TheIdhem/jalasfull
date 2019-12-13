package ir.faj.jalas.jalas.entities

import ir.faj.jalas.jalas.enums.EventLogType
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Type
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "ja_event_log")
@DynamicUpdate
class EventLog(
        @Id
        @Column(name="id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id:Int = 0,

        @Basic
        @Temporal(TemporalType.TIMESTAMP)
        @Column(name="creation_time")
        var creationTime: Date = Date(),

        @Basic
        @Column(name="event_type")
        @Type(type = "ir.faj.jalas.jalas.enums.EventLogTypeClassTypeEnum")
        var eventType: EventLogType = EventLogType.roomNotAvailable,

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "session_id", nullable = false)
        var session: Session = Session(),

        @Basic
        @Column(name = "checked")
        var checked: Boolean = false

)